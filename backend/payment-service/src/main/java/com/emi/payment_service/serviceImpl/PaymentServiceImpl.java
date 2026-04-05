package com.emi.payment_service.serviceImpl;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;

import com.emi.events.payment.PaymentStatus;
import com.emi.payment_service.RequestDtos.GatewayPaymentRequest;
import com.emi.payment_service.RequestDtos.GatewayPayoutRequest;
import com.emi.payment_service.RequestDtos.RequestPaymentDto;
import com.emi.payment_service.RequestDtos.RequestWithdrawDto;
import com.emi.payment_service.ResponseDtos.GatewayResponse;
import com.emi.payment_service.ResponseDtos.ResponseBalanceDto;
import com.emi.payment_service.client.WalletClient;
import com.emi.payment_service.entity.IdempotencyRecord;
import com.emi.payment_service.entity.Payments;
import com.emi.payment_service.enums.IdempotencyStatus;
import com.emi.payment_service.exception.InsufficientBalanceException;
import com.emi.payment_service.gatewayPayment.PaymentGateway;
import com.emi.payment_service.kafka.PaymentEventGeneration;
import com.emi.payment_service.mapper.EventMapper;
import com.emi.payment_service.mapper.GatewayMapper;
import com.emi.payment_service.mapper.IdempotencyMapper;
import com.emi.payment_service.mapper.PaymentMapper;
import com.emi.payment_service.repositories.IdempotencyRepo;
import com.emi.payment_service.repositories.PaymentRepo;
import com.emi.payment_service.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Payout;
import com.stripe.net.Webhook;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	@Value("${webhook.secret.Key}")
	private String webhookSecret;
	private final PaymentGateway paymentGateway;
	private final ObjectMapper objectMapper;
	private final IdempotencyRepo idempRepo;
	private final PaymentRepo paymentRepo;
	private final EventMapper eventMapper;
	private final IdempotencyMapper idempMapper;
	private final PaymentMapper paymentMapper;
	private final GatewayMapper gatewayMapper;
	private final PaymentEventGeneration eventGeneration;
	private final WalletClient walletClient;

	@Override
	public String createIntent(RequestPaymentDto request, UUID idempotencyKey, UUID keycloakId) {
		IdempotencyRecord idempotency = idempMapper.getEntity(request, idempotencyKey, keycloakId);

		try {
			idempRepo.save(idempotency);
		} catch (DataIntegrityViolationException ex) {
			IdempotencyRecord existing = idempRepo.findByUserKeycloakIdAndIdempotencyKey(keycloakId, idempotencyKey)
					.orElseThrow();

			if (existing.getStatus() == IdempotencyStatus.COMPLETED) {
				try {
					return objectMapper.readValue(existing.getResponseBody(), String.class);
				} catch (JsonProcessingException e) {
					throw new RuntimeException("Failed to deserialize JSON", e);
				}
			}
			throw new IllegalStateException("Request already in progress");
		}
		
		Payments payment = paymentMapper.toEntity(request, keycloakId);
		paymentRepo.save(payment);

		GatewayPaymentRequest gatewayRequest = gatewayMapper.getRequest(payment, idempotencyKey);
		GatewayResponse gatewayResponse = paymentGateway.charge(gatewayRequest);
		
		payment.setGatewayTransactionId(gatewayResponse.transactionId());
		paymentRepo.save(payment);

		idempMapper.updateIdemp(idempotency, gatewayResponse.client_secret());
		idempRepo.save(idempotency);
		
		return gatewayResponse.client_secret();
	}

	@Override
	public void payout(RequestWithdrawDto request, UUID idempotencyKey, UUID keycloakId) {
		
		Payments payment = paymentMapper.toEntityWithdraw(request, keycloakId);
		paymentRepo.save(payment);	
		
		ResponseBalanceDto balanceDto = walletClient.getBalance();
		
		if(balanceDto.balance().compareTo(request.amount())> 0){
			throw new InsufficientBalanceException("Withdrawal amount exceeds wallet balance. Available: " + balanceDto.balance());
		}

		GatewayPayoutRequest gatewayRequest = gatewayMapper.getRequestPayout(payment, idempotencyKey, request.destinationAccountId());
		GatewayResponse gatewayResponse = paymentGateway.payout(gatewayRequest, keycloakId);
		
		payment.setGatewayTransactionId(gatewayResponse.transactionId());

	
	}

	@Override
	public String cancelPayment(String transactionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleWebhook(String payload, String sigHeader) {
		Event event;

		try {
			event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
		} catch (SignatureVerificationException ex) {
			throw new RuntimeException("Invalid Stripe Signature");
		}

		handleEvent(event);
	}
	
	private void handleEvent(Event event) {

		switch (event.getType()) {
		case "payment_intent.succeeded" -> handleSuccess(event);

		case "payment_intent.payment_failed" -> handleFailure(event);
		
        case "payout.paid" -> handlePayoutSuccess(event);

        case "payout.failed" -> handlePayoutFailure(event);
		}
	}	

	private void handlePayoutFailure(Event event) {
	  Payout payout = (Payout) event.getDataObjectDeserializer()
	            .getObject()
	            .orElseThrow();

	    String transactionId = payout.getId();

	    Payments payment = paymentRepo
	            .findByGatewayTransactionId(transactionId);

	    if (payment.getStatus() == PaymentStatus.SUCCESS) {
	        return;
	    }

	    payment.setStatus(PaymentStatus.SUCCESS);
	    payment.setUpdatedAt(Instant.now());

	    paymentRepo.save(payment);

	    eventGeneration.paymentWithdrawSuccess(
	            eventMapper.getEventPayoutSuccess(payment)
	    );
	}

	private void handlePayoutSuccess(Event event) {
	    Payout payout = (Payout) event.getDataObjectDeserializer()
	            .getObject()
	            .orElseThrow();

	    String transactionId = payout.getId();

	    Payments payment = paymentRepo
	            .findByGatewayTransactionId(transactionId);

	    if (payment.getStatus() == PaymentStatus.SUCCESS ||
	        payment.getStatus() == PaymentStatus.FAILED) {
	        return;
	    }

	    payment.setStatus(PaymentStatus.FAILED);
	    payment.setUpdatedAt(Instant.now());

	    paymentRepo.save(payment);

	    eventGeneration.paymentWithdrawFailed(
	            eventMapper.getEventPayoutFailure(payment)
	    );
	}

	@Transactional
	private void handleFailure(Event event) {
		
		PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElseThrow();

		String transactionId = intent.getId();

		Payments payment = paymentRepo.findByGatewayTransactionId(transactionId);

		if (payment.getStatus() == PaymentStatus.SUCCESS ||
				payment.getStatus() == PaymentStatus.FAILED) {
			return;
		}
		
		payment.setStatus(PaymentStatus.FAILED);
		payment.setUpdatedAt(Instant.now());
		paymentRepo.save(payment);
		
		eventGeneration.paymentFailed(eventMapper.getEvents(payment));
	}

	@Transactional
	private void handleSuccess(Event event) {
		PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElseThrow();

		String transactionId = intent.getId();

		Payments payment = paymentRepo.findByGatewayTransactionId(transactionId);

		if (payment.getStatus() == PaymentStatus.SUCCESS) {
			return;
		}

		payment.setStatus(PaymentStatus.SUCCESS);
		payment.setUpdatedAt(Instant.now());
		paymentRepo.save(payment);

		eventGeneration.paymentSuccess(eventMapper.getEvents(payment));;

	}

}
