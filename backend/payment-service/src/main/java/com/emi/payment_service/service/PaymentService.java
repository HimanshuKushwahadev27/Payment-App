package com.emi.payment_service.service;

import java.util.UUID;

import com.emi.payment_service.RequestDtos.RequestPaymentDto;
import com.emi.payment_service.RequestDtos.RequestWithdrawDto;
import com.emi.payment_service.ResponseDtos.ResponsePaymentDto;
import com.emi.payment_service.ResponseDtos.WithdrawResponseDto;

public interface PaymentService {

	ResponsePaymentDto charge(RequestPaymentDto request,  UUID idempotencyKey, UUID keycloakId);
    
	WithdrawResponseDto payout(RequestWithdrawDto request, UUID idempotencyKey, UUID keycloakId);
	
	ResponsePaymentDto cancelPayment(String transactionId);
	
	void handleWebhook(String payload, String sigHeader);
	
}
