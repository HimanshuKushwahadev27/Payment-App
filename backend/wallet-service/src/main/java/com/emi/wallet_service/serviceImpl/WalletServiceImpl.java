package com.emi.wallet_service.serviceImpl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.emi.events.payment.PaymentEvent;
import com.emi.events.payment.PaymentStatus;
import com.emi.events.transactions.TransactionEvent;
import com.emi.events.transactions.TransactionStatus;
import com.emi.wallet_service.RequestDtos.CreateAccountDto;
import com.emi.wallet_service.RequestDtos.TransferRequestDto;
import com.emi.wallet_service.ResponseDto.ReponseBalanceDto;
import com.emi.wallet_service.ResponseDto.ResponseAccountDto;
import com.emi.wallet_service.entity.Account;
import com.emi.wallet_service.entity.IdempotencyKeys;
import com.emi.wallet_service.entity.ProcessedEvents;
import com.emi.wallet_service.entity.WalletBalance;
import com.emi.wallet_service.enums.IdempotencyStatus;
import com.emi.wallet_service.exception.AccountNotExistsException;
import com.emi.wallet_service.exception.UnauthorizedException;
import com.emi.wallet_service.exception.WalletNotExistsException;
import com.emi.wallet_service.mapper.AccountMapper;
import com.emi.wallet_service.mapper.IdempotencyMapper;
import com.emi.wallet_service.mapper.WalletBalanceMapper;
import com.emi.wallet_service.repositories.AccountRepo;
import com.emi.wallet_service.repositories.IdempotencyRepo;
import com.emi.wallet_service.repositories.ProcessedEventRepo;
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
	private final ProcessedEventRepo processedEventRepo;
	
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
	public void charge(PaymentEvent event) {
		if(processedEventRepo.existsById(UUID.fromString((String)event.getEventId()))) {
			return;
		}
		
		ProcessedEvents processed = new ProcessedEvents();
		processed.setId(UUID.fromString((String)event.getEventId()));
		processedEventRepo.save(processed);
		
		if(event.getPaymentStatus() != PaymentStatus.SUCCESS){
			throw new IllegalArgumentException("payment isn't succeeded");
		}
		
		UUID userId = UUID.fromString((String)event.getUserKeycloakId());
		Account account = accountRepo
				.findByUserKeycloakId(userId)
				.orElseThrow(
						() -> new AccountNotExistsException("you havent created an account yet")
						);
		
		if( !balanceRepo.existsByAccountId(account.getId())) {
			throw new WalletNotExistsException("u havent created an wallet yet");
		}
		
		WalletBalance wallet = balanceRepo.findByAccountId(account.getId());
		
		BigDecimal amount = BigDecimal.valueOf(event.getAmount()).divide(BigDecimal.valueOf(100));
		
		wallet.setBalance(wallet.getBalance().add(amount));
		wallet.setUpdatedAt(Instant.now());
		balanceRepo.save(wallet);
		
		
	}

	@Override
	public void payout(TransactionEvent event) {
		if(processedEventRepo.existsById(UUID.fromString((String)event.getEventId()))) {
			return;
		}
		
		ProcessedEvents processed = new ProcessedEvents();
		processed.setId(UUID.fromString((String)event.getEventId()));
		processedEventRepo.save(processed);
		
		if(event.getTransactionStatus() != TransactionStatus.SUCCESS){
			throw new IllegalArgumentException("transaction isn't succeeded");
		}
		
		UUID userId = UUID.fromString((String)event.getUserKeycloakId());
		Account account = accountRepo
				.findByUserKeycloakId(userId)
				.orElseThrow(
						() -> new AccountNotExistsException("you havent created an account yet")
				
						);
		BigDecimal amount = BigDecimal.valueOf(event.getAmount()).divide(BigDecimal.valueOf(100));
		WalletBalance wallet = balanceRepo.findByAccountId(account.getId());
		
	    if (wallet.getBalance().compareTo(amount) < 0) {
	        throw new RuntimeException("Insufficient balance");
	    }
	    
	    wallet.setBalance(wallet.getBalance().subtract(amount));
		wallet.setUpdatedAt(Instant.now());

		balanceRepo.save(wallet);
	}

	@Override
	public void transfer(TransferRequestDto request, UUID keycloakId) {
		
		if(!accountRepo.existsByUserKeycloakId(keycloakId)) {
			throw new  AccountNotExistsException("you havent created an account yet");
		}
		
		WalletBalance walletSender = balanceRepo.findByAccountId(request.fromAccountId());	
		WalletBalance walletReceiver = balanceRepo.findByAccountId(request.toAccountId());
		
	    if (walletSender.getBalance().compareTo(request.amount()) < 0) {
	        throw new RuntimeException("Insufficient balance");
	    }
	    
	    walletSender.setBalance(walletSender.getBalance().subtract(request.amount()));
	    walletReceiver.setBalance(walletReceiver.getBalance().add(request.amount()));
	    
	    walletSender.setUpdatedAt(Instant.now());
	    walletReceiver.setUpdatedAt(Instant.now());
	    
	    balanceRepo.save(walletSender);
	    balanceRepo.save(walletReceiver);
	}

	@Override
	public ReponseBalanceDto getBalance(UUID accountId, UUID userKeycloakId) {
		Account account = accountRepo.findById(accountId).orElseThrow(() -> new AccountNotExistsException("Not found the account"));
		
		if(!account.getUserKeycloakId().equals(userKeycloakId)) {
			throw new UnauthorizedException("Not ur wallet");
		}
		
		WalletBalance balance = balanceRepo.findByAccountId(accountId);
		
		return balanceMapper.toDto(balance);
		
	}



}
