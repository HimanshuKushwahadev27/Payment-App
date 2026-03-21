package com.emi.wallet_service.mapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.emi.events.payment.PaymentStatus;
import com.emi.events.transactions.TransactionEvent;
import com.emi.wallet_service.ResponseDto.LedgerResponseDto;
import com.emi.wallet_service.ResponseDto.PayoutAccountResponse;
import com.emi.wallet_service.entity.Account;
import com.emi.wallet_service.entity.LedgerEntry;
import com.emi.wallet_service.enums.EntryType;

@Component
public class LedgerMapper {

	@Value("${system.acc.id}")
	private String  SYSTEM_ACC_ID;

	public LedgerEntry toEntityPayout(TransactionEvent event, Account account) {
		LedgerEntry entry = new LedgerEntry();
		entry.setTransactionId(UUID.fromString((String)event.getTransactionId()));
		entry.setToAccountId((String)event.getToAccountId());
		entry.setFromAccountId(SYSTEM_ACC_ID);
		entry.setEntryType(EntryType.DEBIT);
		entry.setAmount(BigDecimal.valueOf(event.getAmount()));
		entry.setCreatedAt(Instant.now());
		entry.setStatus(PaymentStatus.SUCCESS);
		return entry;
	}
	
	public LedgerEntry toEntityDeposit(TransactionEvent event, PayoutAccountResponse account) {
		LedgerEntry entry = new LedgerEntry();
		entry.setTransactionId(UUID.fromString((String)event.getTransactionId()));
		entry.setToAccountId(account.destinationAccountId());
		entry.setFromAccountId(SYSTEM_ACC_ID);
		entry.setEntryType(EntryType.CREDIT);
		entry.setAmount(BigDecimal.valueOf(event.getAmount()));
		entry.setCreatedAt(Instant.now());
		entry.setStatus(PaymentStatus.SUCCESS);
		return entry;
	}

	public LedgerResponseDto toDto(LedgerEntry event) {
		return new LedgerResponseDto(
				event.getTransactionId(),
				event.getToAccountId(),
				event.getFromAccountId(),
				event.getEntryType(),
				event.getCreatedAt(),
				event.getAmount(),
				event.getStatus()
				);
	}

	public LedgerEntry toEntityPayoutFailure(TransactionEvent event, Account account) {
		LedgerEntry entry = this.toEntityPayout(event, account);
		
		entry.setStatus(PaymentStatus.FAILED);
		
		return entry;
	}

	public LedgerEntry toEntityDepositFailure(TransactionEvent event, PayoutAccountResponse account) {
		LedgerEntry entry  = this.toEntityDeposit(event, account);
		entry.setStatus(PaymentStatus.FAILED);
		return entry;
	}

}
