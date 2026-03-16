package com.emi.wallet_service.service;

import java.util.UUID;

import com.emi.wallet_service.RequestDtos.CreateAccountDto;
import com.emi.wallet_service.ResponseDto.ReponseBalanceDto;
import com.emi.wallet_service.ResponseDto.ResponseAccountDto;

public interface WalletService {

	public ResponseAccountDto createAccount(CreateAccountDto request, UUID idempotencyKey, UUID keycloakId);
	
	public void charge();
	
	public void payout();
	
	public void transfer();
	
	public ReponseBalanceDto getBalance(UUID accountId);
}
