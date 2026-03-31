package com.emi.wallet_service.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emi.wallet_service.ResponseDto.PayoutAccountResponse;
import com.emi.wallet_service.service.UserPayoutAccountService;
import com.stripe.exception.StripeException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wallet/stored_accounts")
@RequiredArgsConstructor
public class UserPayoutAccountContoller {

	
	private final UserPayoutAccountService accountService;


    @GetMapping("/onboarding-link")
    public ResponseEntity<String> getOnboardingLink(
          @AuthenticationPrincipal Jwt jwt) throws StripeException {
      return ResponseEntity.ok(accountService.createOnboardingLink(
          UUID.fromString(jwt.getSubject())
      ));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
      accountService.handleWebhook(payload, sigHeader);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status")
    public ResponseEntity<PayoutAccountResponse> getStatus (
            @AuthenticationPrincipal Jwt jwt) throws StripeException{

        UUID userId = UUID.fromString(jwt.getSubject());

        return ResponseEntity.ok(
            accountService.getStripeStatus(userId)
        );
    }
}
