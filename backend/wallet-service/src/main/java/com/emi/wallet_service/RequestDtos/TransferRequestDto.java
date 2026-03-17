package com.emi.wallet_service.RequestDtos;

import java.math.BigDecimal;

public record TransferRequestDto(
		
		String toAccountId,
		String fromAccountId,
		BigDecimal amount 

		) {

}
