package com.emi.wallet_service.RequestDtos;

import java.util.UUID;

import com.emi.wallet_service.enums.AccountType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request payload for creating a new account")
public record CreateAccountDto(

        @Schema(
                description = "Unique identifier of the user",
                example = "b7c4c84e-45c2-4e3a-9a7a-3b3f3e6d8a8a",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        UUID userId,

        @Schema(
                description = "Type of account",
                example = "WALLET",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        AccountType type,

        @Schema(
                description = "ISO currency code",
                example = "INR",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String currency
) {}