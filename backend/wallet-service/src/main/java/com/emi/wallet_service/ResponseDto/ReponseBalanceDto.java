package com.emi.wallet_service.ResponseDto;

import java.math.BigDecimal;
import java.time.Instant;

public record ReponseBalanceDto(
		BigDecimal balance,
		Instant updateAt
		) {

}
