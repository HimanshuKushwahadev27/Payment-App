package com.emi.wallet_service.serviceImpl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.emi.events.transactions.TransactionEvent;
import com.emi.events.transactions.TransactionStatus;
import com.emi.wallet_service.ResponseDto.LedgerResponseDto;
import com.emi.wallet_service.ResponseDto.PayoutAccountResponse;
import com.emi.wallet_service.entity.Account;
import com.emi.wallet_service.entity.LedgerEntry;
import com.emi.wallet_service.entity.ProcessedEvents;
import com.emi.wallet_service.exception.AccountNotExistsException;
import com.emi.wallet_service.exception.LedgerNotFoundException;
import com.emi.wallet_service.mapper.LedgerMapper;
import com.emi.wallet_service.repositories.AccountRepo;
import com.emi.wallet_service.repositories.LedgerRepo;
import com.emi.wallet_service.repositories.ProcessedEventRepo;
import com.emi.wallet_service.service.LedgerService;
import com.emi.wallet_service.service.UserPayoutAccountService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LedgerServiceImpl implements LedgerService {
	
	private final UserPayoutAccountService payoutAccountService;
	private final LedgerMapper ledgerMapper;
	private final ProcessedEventRepo processedEventRepo;
	private final AccountRepo accountRepo;
	private final LedgerRepo ledgerRepo;
	
	@Override
	public void updateLedgerPayout(TransactionEvent event) {
		
		if(event.getTransactionStatus()!= TransactionStatus.SUCCESS) {
			throw new IllegalArgumentException("Transactiob not completed yet");
		}
		
		
		if(processedEventRepo.existsById(UUID.fromString((String)event.getEventId()))) {
			return;
		}
		
		ProcessedEvents processed = new ProcessedEvents();
		processed.setId(UUID.fromString((String)event.getEventId()));
		processedEventRepo.save(processed);
		
		UUID userId = UUID.fromString((String)event.getUserKeycloakId());
		Account account = accountRepo
				.findByUserKeycloakId(userId)
				.orElseThrow(
						() -> new AccountNotExistsException("you havent created an account yet")
						);
		LedgerEntry entry = ledgerMapper.toEntityPayout(event,account);
		ledgerRepo.save(entry);
	}

	@Override
	public void updateLedgerDeposit(TransactionEvent event) {
		
		if(event.getTransactionStatus()!= TransactionStatus.SUCCESS) {
			throw new IllegalArgumentException("Transactiob not completed yet");
		}
		
		if(processedEventRepo.existsById(UUID.fromString((String)event.getEventId()))) {
			return;
		}
		
		ProcessedEvents processed = new ProcessedEvents();
		processed.setId(UUID.fromString((String)event.getEventId()));
		processedEventRepo.save(processed);
		
		UUID userId = UUID.fromString((String)event.getUserKeycloakId());
		PayoutAccountResponse account = payoutAccountService.getDefaultAccount(userId);
		LedgerEntry entry = ledgerMapper.toEntityDeposit(event,account);
		ledgerRepo.save(entry);
	}

	@Override
	public LedgerResponseDto getUsersRecord(UUID transaction_Id) {
		LedgerEntry event = ledgerRepo
				.findByTransactionId()
				.orElseThrow(() -> new LedgerNotFoundException("ledger not exists for the given transaction"));
	
		return ledgerMapper.toDto(event);
	}

	@Override
	public void updateLedgerPayoutFailure(TransactionEvent event) {
		if(event.getTransactionStatus()!= TransactionStatus.SUCCESS) {
			throw new IllegalArgumentException("Transactiob not completed yet");
		}
		
		
		if(processedEventRepo.existsById(UUID.fromString((String)event.getEventId()))) {
			return;
		}
		
		ProcessedEvents processed = new ProcessedEvents();
		processed.setId(UUID.fromString((String)event.getEventId()));
		processedEventRepo.save(processed);
		
		UUID userId = UUID.fromString((String)event.getUserKeycloakId());
		Account account = accountRepo
				.findByUserKeycloakId(userId)
				.orElseThrow(
						() -> new AccountNotExistsException("you havent created an account yet")
						);
		LedgerEntry entry = ledgerMapper.toEntityPayoutFailure(event,account);
		ledgerRepo.save(entry);
		
	}

	@Override
	public void updateLedgerDepositFailure(TransactionEvent event) {
		
		if(event.getTransactionStatus()!= TransactionStatus.SUCCESS) {
			throw new IllegalArgumentException("Transactiob not completed yet");
		}
		
		if(processedEventRepo.existsById(UUID.fromString((String)event.getEventId()))) {
			return;
		}
		
		ProcessedEvents processed = new ProcessedEvents();
		processed.setId(UUID.fromString((String)event.getEventId()));
		processedEventRepo.save(processed);
		
		UUID userId = UUID.fromString((String)event.getUserKeycloakId());
		PayoutAccountResponse account = payoutAccountService.getDefaultAccount(userId);

		LedgerEntry entry = ledgerMapper.toEntityDepositFailure(event,account);
		ledgerRepo.save(entry);
		
	}

}
