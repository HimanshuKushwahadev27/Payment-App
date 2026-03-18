package com.emi.transaction_service.service;

import java.util.List;
import java.util.UUID;

import com.emi.events.payment.PaymentEvent;
import com.emi.events.transactions.TransactionEvent;
import com.emi.transaction_service.requestDtos.TransactionPayoutRequestDto;
import com.emi.transaction_service.responseDtos.TransactionResponseDto;

public interface TransactionService {

	public TransactionResponseDto payout(TransactionPayoutRequestDto request, UUID IdempotencyKey, UUID userKeycloakId);
	
	public List<TransactionResponseDto> getAll(UUID userKeycloakId);
	
    public TransactionResponseDto get(UUID keycloakId, UUID transactionId);
    
	
	public void payoutEventSuccess(TransactionEvent event);
	
	public void payoutEventFailure(TransactionEvent event);
	
	public void paymentEventFailure(PaymentEvent event);
	
	public void paymentEventSuccess(PaymentEvent event);
}
