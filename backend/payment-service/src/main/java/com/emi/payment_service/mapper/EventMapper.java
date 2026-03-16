package com.emi.payment_service.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.emi.events.payment.PaymentEvent;
import com.emi.events.transactions.TransactionEvent;
import com.emi.events.transactions.TransactionStatus;
import com.emi.events.transactions.TransactionType;
import com.emi.payment_service.entity.Payments;

@Component
public class EventMapper {

	public PaymentEvent getEvents(Payments payment) {
		return new PaymentEvent(
				UUID.randomUUID().toString(),
				payment.getId().toString(),
				payment.getUserKeycloakId().toString(),
				payment.getCurrency(),
				payment.getAmount().longValue(),
				payment.getPaymentType(),
				payment.getStatus(),
				payment.getPaymentMethodType(),
				payment.getCreatedAt().toEpochMilli()
				);
	}

	public TransactionEvent getEventPayoutSuccess(Payments payment) {
		return new TransactionEvent(
				UUID.randomUUID().toString(),
				payment.getPayoutTransactionId().toString(),
				payment.getCurrency(),
				payment.getToAccountId().toString(),
				payment.getUserKeycloakId().toString(),
				payment.getAmount().longValue(),
				TransactionType.WITHDRAW,
				TransactionStatus.SUCCESS
				);
	}
	
	public TransactionEvent getEventPayoutFailure(Payments payment) {
		return new TransactionEvent(
				UUID.randomUUID().toString(),
				payment.getPayoutTransactionId().toString(),
				payment.getCurrency(),
				payment.getToAccountId().toString(),
				payment.getUserKeycloakId().toString(),
				payment.getAmount().longValue(),
				TransactionType.WITHDRAW,
				TransactionStatus.FAILED
				);
	}


}
