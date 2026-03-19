package com.emi.transaction_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.emi.events.payment.PaymentEvent;
import com.emi.events.transactions.TransactionEvent;
import com.emi.transaction_service.service.TransactionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventConsume {

	private final TransactionService transactionService;
	
	@KafkaListener(topics ="Payment-withdraw-success-event" ,groupId="TransactionService")
	public void recordPayoutSuccess(TransactionEvent event) {
		transactionService.payoutEventSuccess(event);
	}
	
	
	@KafkaListener(topics ="Payment-withdraw-failed-event" ,groupId="TransactionService")
	public void recordPayoutFailure(TransactionEvent event) {
		transactionService.payoutEventFailure(event);
	}
	
	@KafkaListener(topics ="Payment-success-event" ,groupId="TransactionService")
	public void recordPaymentSuccess(PaymentEvent event) {
		transactionService.paymentEventSuccess(event);
	}
	
	@KafkaListener(topics ="Payment-failed-event" ,groupId="TransactionService")
	public void recordPaymentFailure(PaymentEvent event) {
		transactionService.paymentEventFailure(event);
	}
	
}
