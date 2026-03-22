package com.emi.payment_service.ResponseDtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO representing payment details returned from Stripe")
public record StripeResponse(

        @Schema(
            description = "Unique identifier of the Stripe PaymentIntent",
            example = "pi_3Nk2aL2eZvKYlo2C1abcd123"
        )
        String id,

        @Schema(
            description = "Current status of the payment",
            example = "succeeded"
        )
        String status,

        @Schema(
            description = "Amount in the smallest currency unit (e.g., paise for INR, cents for USD)",
            example = "49900"
        )
        Long amount,

        @Schema(
            description = "Currency code in ISO format",
            example = "inr"
        )
        String currency,

        @Schema(
            description = "Timestamp when the payment was created (Unix epoch time in seconds)",
            example = "1710000000"
        )
        Long created,

        @Schema(
            description = "Client secret used by frontend to complete the payment",
            example = "pi_3Nk2aL2eZvKYlo2C1abcd123_secret_xyz"
        )
        String client_secret

) {}