package com.emi.transaction_service.responseDtos;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.emi.events.transactions.TransactionStatus;
import com.emi.events.transactions.TransactionType;

public record TransactionResponseDto(
		
		
		UUID id ,
		String toAccId,
		BigDecimal amt,
		String currency,
		TransactionType type,
		TransactionStatus status,
		Instant updatedAt
		) {

}
