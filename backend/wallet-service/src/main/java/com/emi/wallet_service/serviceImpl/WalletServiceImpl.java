package com.emi.wallet_service.serviceImpl;

import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


import com.emi.wallet_service.RequestDtos.CreateAccountDto;
import com.emi.wallet_service.ResponseDto.ReponseBalanceDto;
import com.emi.wallet_service.ResponseDto.ResponseAccountDto;
import com.emi.wallet_service.entity.Account;
import com.emi.wallet_service.entity.IdempotencyKeys;
import com.emi.wallet_service.entity.LedgerEntry;
import com.emi.wallet_service.entity.WalletBalance;
import com.emi.wallet_service.enums.IdempotencyStatus;
import com.emi.wallet_service.mapper.AccountMapper;
import com.emi.wallet_service.mapper.IdempotencyMapper;
import com.emi.wallet_service.mapper.LedgerMapper;
import com.emi.wallet_service.mapper.WalletBalanceMapper;
import com.emi.wallet_service.repositories.AccountRepo;
import com.emi.wallet_service.repositories.IdempotencyRepo;
import com.emi.wallet_service.repositories.WalletBalanceRepo;
import com.emi.wallet_service.service.WalletService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService{

	private final ObjectMapper objectMapper;
	private final IdempotencyRepo idempotencyRepo;
	private final IdempotencyMapper idempotencyMapper;
	private final AccountMapper accountMapper;
	private final WalletBalanceMapper balanceMapper;
	private final AccountRepo accountRepo;
	private final WalletBalanceRepo balanceRepo;
	private final LedgerMapper ledgerMapper;
	@Override
	public ResponseAccountDto createAccount(CreateAccountDto request, UUID idempotencyKey, UUID userKeycloakId) {
		
		IdempotencyKeys idempotency = idempotencyMapper.toEntity(request,idempotencyKey, userKeycloakId);
		
		try {
			idempotencyRepo.save(idempotency);
		} catch (DataIntegrityViolationException ex) {
			IdempotencyKeys existing = idempotencyRepo.findByUserKeycloakIdAndIdempotencyKey(userKeycloakId, idempotencyKey)
					.orElseThrow();

			if (existing.getStatus() == IdempotencyStatus.COMPLETED) {
				try {
					return objectMapper.readValue(existing.getResponseBody(), ResponseAccountDto.class);
				} catch (JsonProcessingException e) {
					throw new RuntimeException("Failed to deserialize JSON", e);
				}
			}
			throw new IllegalStateException("Request already in progress");
		}
		
		
		Account account = accountMapper.toEntity(request, userKeycloakId);
		
		accountRepo.save(account);
		
		WalletBalance balance = balanceMapper.toEntity(account);
		balanceRepo.save(balance);
				
		return accountMapper.toDto(account);
		
	}

	@Override
	public void charge() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void payout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transfer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ReponseBalanceDto getBalance(UUID accountId) {
		// TODO Auto-generated method stub
		return null;
	}



}
