package com.emi.payment_service.kafka;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.emi.events.transactions.TransactionEvent;
import com.emi.payment_service.RequestDtos.RequestWithdrawDto;
import com.emi.payment_service.mapper.PaymentMapper;
import com.emi.payment_service.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsumeTransactionEvent {

	private final PaymentService paymentService;
	private final PaymentMapper paymentMapper;
	
	@KafkaListener(topics ="Payout-generation-event" ,groupId="PaymentService")
	public void callPayout(TransactionEvent event) {
		
		RequestWithdrawDto request = paymentMapper.toRequestWithdraw(event);
		
		paymentService.payout(request, UUID.fromString((String)event.getEventId()), UUID.fromString((String)event.getUserKeycloakId()));
	}

}
