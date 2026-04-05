package com.emi.payment_service.ResponseDtos;

import java.math.BigDecimal;
import java.time.Instant;

public record ResponseBalanceDto(
  		BigDecimal balance,
		Instant updateAt
) {
  
}
