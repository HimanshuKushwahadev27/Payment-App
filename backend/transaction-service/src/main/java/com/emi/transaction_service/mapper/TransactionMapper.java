package com.emi.transaction_service.mapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.emi.events.payment.PaymentEvent;
import com.emi.events.transactions.TransactionStatus;
import com.emi.events.transactions.TransactionType;
import com.emi.transaction_service.entity.Transaction;
import com.emi.transaction_service.requestDtos.TransactionPayoutRequestDto;
import com.emi.transaction_service.responseDtos.TransactionResponseDto;

@Component
public class TransactionMapper {

	@Value("${system.acc.id}")
	private String  SYSTEM_ACC_ID;
	
	
	public Transaction toEntity(UUID keycloakId, TransactionPayoutRequestDto request) {
		Transaction transaction = new Transaction();
		transaction.setAmount(request.amount());
		transaction.setCreatedAt(Instant.now());
		transaction.setUpdatedAt(Instant.now());
		transaction.setCurrency(request.currency());
		transaction.setFromAccountId(SYSTEM_ACC_ID);
		transaction.setToAccountId(request.recieversAccId());
		transaction.setUserKeycloakId(keycloakId);
		transaction.setType(TransactionType.WITHDRAW);
		transaction.setStatus(TransactionStatus.INITIATED);
		transaction.setUpdatedAt(Instant.now());
		return transaction;
	}


	public TransactionResponseDto toDto(Transaction transaction) {
		return new TransactionResponseDto(
				transaction.getId(),
				transaction.getToAccountId(),
				transaction.getAmount(),
				transaction.getCurrency(),
				transaction.getType(),
				transaction.getStatus(),
				transaction.getUpdatedAt()
				);
	}


	public Transaction toEntityDepositSuccess(PaymentEvent event) {
		Transaction transaction = new Transaction();
		
		transaction.setUserKeycloakId(UUID.fromString(event.getUserKeycloakId().toString()));
		transaction.setAmount(BigDecimal.valueOf(event.getAmount()));
		transaction.setUpdatedAt(Instant.now());
		transaction.setCreatedAt(Instant.now());
		transaction.setCurrency((String)event.getCurrency());
		transaction.setStatus(TransactionStatus.SUCCESS);
		transaction.setType(TransactionType.DEPOSIT);
		transaction.setToAccountId("");
		return transaction;
	}


	public Transaction toEntityDepositFailure(PaymentEvent event) {
		Transaction transaction = new Transaction();
		
		transaction.setUserKeycloakId(UUID.fromString(event.getUserKeycloakId().toString()));
		transaction.setAmount(BigDecimal.valueOf(event.getAmount()));
		transaction.setUpdatedAt(Instant.now());
		transaction.setCreatedAt(Instant.now());
		transaction.setToAccountId("");
		transaction.setCurrency((String)event.getCurrency());
		transaction.setStatus(TransactionStatus.FAILED);
		transaction.setType(TransactionType.DEPOSIT);
		return transaction;

	}

}
