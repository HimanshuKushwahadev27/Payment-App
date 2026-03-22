package com.emi.wallet_service.RequestDtos;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferRequestDto(
		
		UUID toAccountId,
		UUID fromAccountId,
		BigDecimal amount 

		) {

}
