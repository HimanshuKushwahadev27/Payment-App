package com.emi.wallet_service.service;

import java.util.UUID;

import com.emi.wallet_service.ResponseDto.PayoutAccountResponse;
import com.stripe.exception.StripeException;

public interface UserPayoutAccountService {

    String handleWebhook(String payload, String header);
    String createOnboardingLink(UUID fromString) throws StripeException;
    PayoutAccountResponse getStripeStatus(UUID userId) throws StripeException;
}
