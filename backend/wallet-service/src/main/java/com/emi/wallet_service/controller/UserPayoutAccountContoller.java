package com.emi.wallet_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emi.wallet_service.RequestDtos.BankTokenRequest;
import com.emi.wallet_service.ResponseDto.PayoutAccountResponse;
import com.emi.wallet_service.service.UserPayoutAccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wallet/stored_accounts")
@RequiredArgsConstructor
public class UserPayoutAccountContoller {

	
	private final UserPayoutAccountService accountService;
	
	
	@GetMapping("/account")
	ResponseEntity<List<PayoutAccountResponse>> getUsersAccount(@AuthenticationPrincipal Jwt  jwt){
		return ResponseEntity.ok(accountService.getUsersAccount(UUID.fromString(jwt.getSubject())));
	}
	
	@GetMapping("/default_account")
	ResponseEntity<PayoutAccountResponse> getDefaultAccount(@AuthenticationPrincipal Jwt  jwt) {
		return ResponseEntity.ok(accountService.getDefaultAccount(UUID.fromString(jwt.getSubject())));
    }
    
    @DeleteMapping("/delete/{id}")
    void delete(@PathVariable UUID id) {
    	accountService.delete(id);
    }

	@PatchMapping("/setDefault/{accountId}")
    void setDefault(@AuthenticationPrincipal Jwt  jwt, UUID accountId) {
		accountService.setDefault(UUID.fromString(jwt.getSubject()), accountId);
	}

		@PostMapping("/bank-details")
		ResponseEntity<PayoutAccountResponse> storeDetails(
				  @RequestBody @Valid BankTokenRequest request,
					@AuthenticationPrincipal Jwt jwt,
					@RequestHeader("Idempotency-key") UUID idempotencyKey) {
			return ResponseEntity.ok(
					accountService.storeBankDetails(request, idempotencyKey, UUID.fromString(jwt.getSubject()))
			);
   }
}
