package com.emi.payment_service.RequestDtos;



import java.math.BigDecimal;

import com.emi.events.payment.PaymentMethodType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request DTO to initiate payment for a specific order")
public record RequestPaymentDto(
		
		
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "1.00", inclusive = true, message = "Amount must be greater than or equal to 1.00")
        @Schema(
            description = "Payment amount to be withdraw",
            example = "499.99",
            minimum = "1.00"
        )
        BigDecimal amount,
        
        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String currency,
        
        @NotBlank
        @Schema(
            description = "type of method used for payment",
            example = " CARD , UPI, NET BANKING, WALLET"
        )
        PaymentMethodType type 

) {}