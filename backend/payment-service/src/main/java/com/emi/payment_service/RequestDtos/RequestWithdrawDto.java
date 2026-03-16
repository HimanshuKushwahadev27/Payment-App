package com.emi.payment_service.RequestDtos;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RequestWithdrawDto(

		@NotBlank(message = "transaction ID is required")
	    @Size(max = 100, message = "transactionId must not exceed 100 characters")
	
        UUID transactionId,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "1.00", inclusive = true, message = "Amount must be greater than or equal to 1.00")
        BigDecimal amount,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String currency,

        @NotBlank
        String destinationAccountId

) {}