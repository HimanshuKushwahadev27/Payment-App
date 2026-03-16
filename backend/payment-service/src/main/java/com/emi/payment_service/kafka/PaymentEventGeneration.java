package com.emi.payment_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.emi.events.payment.PaymentEvent;
import com.emi.events.transactions.TransactionEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentEventGeneration {

	private final KafkaTemplate<String, PaymentEvent> kafkaEventSuccess;
	private final KafkaTemplate<String, PaymentEvent> kafkaEventFailed;
	private final KafkaTemplate<String, TransactionEvent> kafkaEventPayoutSuccess;
	private final KafkaTemplate<String, TransactionEvent> kafkaEventPayoutWithdraw;
	
	public void paymentSuccess(PaymentEvent event) {
		kafkaEventSuccess.send("Payment-success-event", event);
	}
	
	public void paymentFailed(PaymentEvent event) {
		kafkaEventFailed.send("Payment-failed-event", event);
	}
	
	public void paymentWithdrawSuccess(TransactionEvent event) {
		kafkaEventPayoutSuccess.send("Payment-withdraw-success-event", event);
	}
	
	public void paymentWithdrawFailed(TransactionEvent event) {
		kafkaEventPayoutWithdraw.send("Payment-withdraw-failed-event", event);
	}
}
