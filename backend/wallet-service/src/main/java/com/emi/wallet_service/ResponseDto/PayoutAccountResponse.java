package com.emi.wallet_service.ResponseDto;

import java.time.Instant;
import java.util.UUID;

public record PayoutAccountResponse(
        UUID id,
        UUID userKeycloakId,
        String stripeAccountId,
        Boolean chargesEnabled,
        Boolean payoutsEnabled,
        String bankName,
        String last4,
        Instant createdAt,
        Instant updatedAt
) {

}
