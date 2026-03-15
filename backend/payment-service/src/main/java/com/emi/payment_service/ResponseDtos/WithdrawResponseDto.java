package com.emi.payment_service.ResponseDtos;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.emi.events.payment.PaymentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record WithdrawResponseDto(
		
		  @Schema(
		            description = "Unique identifier of the payment in the system",
		            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
		        )
		        UUID paymentId,


		        @Schema(
		            description = "Payment amount",
		            example = "499.99"
		        )
		        BigDecimal amount,

		        @Schema(
		            description = "Current status of the payment",
		            example = "SUCCESS"
		        )
		        PaymentStatus status,

		        @Schema(
		            description = "Transaction ID returned by the payment gateway (e.g., Stripe)",
		            example = "pi_3Nk2aL2eZvKYlo2C1abcd123"
		        )
		        String gatewayTransactionId,

		        @Schema(
		            description = "Timestamp when the payment was created",
		            type = "string",
		            format = "date-time",
		            example = "2026-03-03T16:30:00Z"
		        )
		        Instant createdAt,

		        @Schema(
		                description = "ISO currency code",
		                example = "INR",
		                requiredMode = Schema.RequiredMode.REQUIRED
		        )
		        @NotBlank
		        @Pattern(regexp = "^[A-Z]{3}$")
		        String currency) {

}
