package com.emi.wallet_service.ResponseDto;

import java.time.Instant;
import java.util.UUID;

public record PayoutAccountResponse(
        UUID id,
        UUID userKeycloakId,
        String destinationAccountId,
        String bankName,
        String last4,
        boolean isDefault,
        Instant createdAt,
        Instant updatedAt) {

}
