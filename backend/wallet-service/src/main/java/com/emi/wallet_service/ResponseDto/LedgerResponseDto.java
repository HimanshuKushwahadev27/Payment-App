package com.emi.wallet_service.ResponseDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.emi.events.payment.PaymentStatus;
import com.emi.wallet_service.enums.EntryType;

public record LedgerResponseDto(
		UUID transactionId,
		String toAcccId,
		String fromAccId,
		EntryType entryType,
		Instant createdAt,
		BigDecimal amount,
		PaymentStatus status
		) {

}
