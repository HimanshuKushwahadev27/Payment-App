package com.emi.payment_service.gatewayPayment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.emi.payment_service.RequestDtos.GatewayPaymentRequest;
import com.emi.payment_service.RequestDtos.GatewayPayoutRequest;
import com.emi.payment_service.RequestDtos.GatewayRefundRequest;
import com.emi.payment_service.ResponseDtos.GatewayResponse;
import com.emi.payment_service.ResponseDtos.StripeResponse;
import com.emi.payment_service.client.UserClient;
import com.emi.payment_service.mapper.GatewayMapper;

import org.springframework.util.MultiValueMap;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StripeGateway implements PaymentGateway {
		
		private final UserClient userClient;
		private final GatewayMapper gatewayMapper;
    private final WebClient stripeWebClient;

    @Override
	public GatewayResponse charge(GatewayPaymentRequest request) {
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		
		body.add("amount", request.amount().multiply(BigDecimal.valueOf(100)).toString());
    body.add("payment_method_types[]", "card");
    body.add("payment_method_types[]", "upi");
		body.add("currency", request.currency().toLowerCase());

		StripeResponse response = stripeWebClient.post()
				.uri("/v1/payment_intents")
			  .header("Idempotency-Key", request.idempotencyKey().toString())	
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)	
			  .bodyValue(body)
				.retrieve()
				.bodyToMono(StripeResponse.class)
				.block()
				;
		
		return new GatewayResponse(
				response.id(),
				gatewayMapper.mapStatus(response.status()),
				gatewayMapper.convertAmount(response.amount()),
						response.currency(),
						response.client_secret()
		);
	}

	@Override
	public GatewayResponse refundPayment(GatewayRefundRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GatewayResponse cancelPayment(String transactionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GatewayResponse payout(GatewayPayoutRequest request, UUID userId) {

	    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

			String stripeAccountId = userClient.getStripeAccId(userId);
			
	    body.add("amount",
	            request.amount()
	                    .multiply(BigDecimal.valueOf(100))
	                    .toString());

	    body.add("currency", request.currency());

	    body.add("destination", stripeAccountId);

	    stripeWebClient.post()
	            .uri("/v1/transfers")
	            .header("Idempotency-Key", "transfer-" +
	                    request.idempotencyKey().toString())
	            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	            .bodyValue(body)
	            .retrieve()
	            .bodyToMono(StripeResponse.class)
	            .block();

				MultiValueMap<String, String> payoutBody = new LinkedMultiValueMap<>();
				payoutBody.add("amount", request.amount()
								.multiply(BigDecimal.valueOf(100))
								.setScale(0, RoundingMode.HALF_UP)
								.toString());
				payoutBody.add("currency", request.currency());
				payoutBody.add("destination", request.destinationAccountId());

				StripeResponse responsePayout = stripeWebClient.post()
								.uri("/v1/payouts")
								.header("Idempotency-Key", "payout-" +
												request.idempotencyKey().toString())
								.header("Stripe-Account", stripeAccountId)
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)
								.bodyValue(payoutBody)
								.retrieve()
								.bodyToMono(StripeResponse.class)
								.block();


	    return new GatewayResponse(
	            responsePayout.id(),
	            gatewayMapper.mapStatus(responsePayout.status()),
	            gatewayMapper.convertAmount(responsePayout.amount()),
	            responsePayout.currency(),
							null
	    );
	}
	

}
