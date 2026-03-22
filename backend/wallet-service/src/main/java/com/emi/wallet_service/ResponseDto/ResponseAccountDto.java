package com.emi.wallet_service.ResponseDto;

import java.time.Instant;
import java.util.UUID;

import com.emi.wallet_service.enums.AccountType;

public record ResponseAccountDto(
		
		
		UUID id,
		Instant createdAt,
		String currency,
		AccountType type
		) {

}
