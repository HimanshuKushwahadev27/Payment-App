package com.emi.payment_service.service;

import java.util.UUID;

import com.emi.payment_service.RequestDtos.RequestPaymentDto;
import com.emi.payment_service.RequestDtos.RequestWithdrawDto;
public interface PaymentService {

	String createIntent(RequestPaymentDto request,  UUID idempotencyKey, UUID keycloakId);
    
	void payout(RequestWithdrawDto request, UUID idempotencyKey, UUID keycloakId);
	
	String cancelPayment(String transactionId);
	
	void handleWebhook(String payload, String sigHeader);
	
}
