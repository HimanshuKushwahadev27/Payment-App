package com.emi.wallet_service.service;

import java.util.UUID;

import com.emi.events.payment.PaymentEvent;
import com.emi.events.transactions.TransactionEvent;
import com.emi.wallet_service.RequestDtos.CreateAccountDto;
import com.emi.wallet_service.RequestDtos.TransferRequestDto;
import com.emi.wallet_service.ResponseDto.ReponseBalanceDto;
import com.emi.wallet_service.ResponseDto.ResponseAccountDto;

public interface WalletService {

	public ResponseAccountDto createAccount(CreateAccountDto request, UUID idempotencyKey, UUID keycloakId);
	
	public void charge(PaymentEvent event);
	
	public void payout(TransactionEvent event);
	
	public void transfer(TransferRequestDto request, UUID keycloakId);
	
	public ResponseAccountDto getAccount(UUID userKeycloakId);

	public ReponseBalanceDto getBalance( UUID keycloakId);
}
