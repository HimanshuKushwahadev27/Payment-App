package com.emi.payment_service.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.emi.events.payment.PaymentEvent;
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


}
