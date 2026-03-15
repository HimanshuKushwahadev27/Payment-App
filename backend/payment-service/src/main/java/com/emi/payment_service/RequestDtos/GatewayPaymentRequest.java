package com.emi.payment_service.RequestDtos;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Request DTO used by API Gateway to initiate a payment")
public record GatewayPaymentRequest(

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "1.00", inclusive = true, message = "Amount must be greater than or equal to 1.00")
        @Schema(
            description = "Payment amount to be charged",
            example = "499.99",
            minimum = "1.00"
        )
        BigDecimal amount,


        @NotBlank(message = "Idempotency key is required")
        @Size(max = 100, message = "Idempotency key must not exceed 100 characters")
        @Schema(
            description = "Unique key to prevent duplicate payment processing",
            example = "idem_abc123xyz"
        )
        String idempotencyKey,

        @Schema(
                description = "ISO currency code",
                example = "INR",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String currency,

        @NotBlank
        @Schema(
            description = "Stripe payment method ID generated on frontend",
            example = "pm_1NqgR7A9abcd123"
        )
        String paymentMethodId

) {}