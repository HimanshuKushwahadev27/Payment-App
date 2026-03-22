package com.emi.payment_service.mapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.emi.events.payment.PaymentMethodType;
import com.emi.events.payment.PaymentStatus;
import com.emi.events.payment.PaymentType;
import com.emi.events.transactions.TransactionEvent;
import com.emi.payment_service.RequestDtos.RequestPaymentDto;
import com.emi.payment_service.RequestDtos.RequestWithdrawDto;
import com.emi.payment_service.ResponseDtos.ResponsePaymentDto;
import com.emi.payment_service.entity.Payments;

@Component
public class PaymentMapper {

	public Payments toEntity(RequestPaymentDto request, UUID keycloakId) {
		Payments payment = new Payments();
		
		payment.setAmount(request.amount());
		payment.setCreatedAt(Instant.now());
		payment.setStatus(PaymentStatus.INITIATED);
		payment.setUserKeycloakId(keycloakId);
		payment.setUpdatedAt(Instant.now());
		payment.setCurrency(request.currency());
		payment.setPaymentMethodType(request.type());
		payment.setPaymentType(PaymentType.DEPOSIT);
		return payment;
	}

	public ResponsePaymentDto toDto(Payments payment, String secret) {
		return new ResponsePaymentDto(
				payment.getId(),
				payment.getAmount(),
				payment.getStatus(),
				payment.getGatewayTransactionId().toString(),
				payment.getCreatedAt(),
				payment.getCurrency(),
				payment.getPaymentMethodType(),
				secret
		)
				;
	}

	public Payments toEntityWithdraw(RequestWithdrawDto request, UUID keycloakId) {
		Payments payment = new Payments();
		
		payment.setAmount(request.amount());
		payment.setCreatedAt(Instant.now());
		payment.setStatus(PaymentStatus.INITIATED);
		payment.setUserKeycloakId(keycloakId);
		payment.setUpdatedAt(Instant.now());
		payment.setCurrency(request.currency());
		payment.setPaymentMethodType(PaymentMethodType.NET_BANKING);
		payment.setPaymentType(PaymentType.DEPOSIT);
		payment.setPayoutTransactionId(request.transactionId());
		payment.setToAccountId(request.destinationAccountId());
		return payment;
	}


	public RequestWithdrawDto toRequestWithdraw(TransactionEvent event) {
		return new RequestWithdrawDto(
				UUID.fromString(event.getTransactionId().toString()),
				BigDecimal.valueOf(event.getAmount()),
				(String)event.getCurrency(),
				(String)event.getToAccountId()
				);
	}
}
