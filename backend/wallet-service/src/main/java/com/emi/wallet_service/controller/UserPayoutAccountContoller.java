package com.emi.wallet_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emi.wallet_service.RequestDtos.CreatePayoutAccountRequest;
import com.emi.wallet_service.RequestDtos.UpdatePayoutAccountRequest;
import com.emi.wallet_service.ResponseDto.PayoutAccountResponse;
import com.emi.wallet_service.service.UserPayoutAccountService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wallet/stored_accounts")
@RequiredArgsConstructor
public class UserPayoutAccountContoller {

	
	private final UserPayoutAccountService accountService;
	
	@PostMapping("/create")
	ResponseEntity<PayoutAccountResponse> create(
			@RequestBody @Valid CreatePayoutAccountRequest request,
			@AuthenticationPrincipal Jwt  jwt,
			@RequestHeader("Idempotency-key") UUID idempotencyKey){
		return ResponseEntity.ok(accountService.create(request, idempotencyKey, UUID.fromString(jwt.getSubject())));
	}
	
	@GetMapping("/account")
	ResponseEntity<List<PayoutAccountResponse>> getUsersAccount(@AuthenticationPrincipal Jwt  jwt){
		return ResponseEntity.ok(accountService.getUsersAccount(UUID.fromString(jwt.getSubject())));
	}
	
	@GetMapping("/default_account")
	ResponseEntity<PayoutAccountResponse> getDefaultAccount(@AuthenticationPrincipal Jwt  jwt) {
		return ResponseEntity.ok(accountService.getDefaultAccount(UUID.fromString(jwt.getSubject())));
    }

	@PatchMapping("/update")
	ResponseEntity<PayoutAccountResponse> update(
    		@AuthenticationPrincipal Jwt  jwt,
    		@RequestBody @Valid UpdatePayoutAccountRequest request) {
    	return ResponseEntity.ok(accountService.update(UUID.fromString(jwt.getSubject()), request));
    }
    
    @DeleteMapping("/delete/{id}")
    void delete(UUID id) {
    	accountService.delete(id);
    }

	@PatchMapping("/setDefault/{accountId}")
    void setDefault(@AuthenticationPrincipal Jwt  jwt, UUID accountId) {
		accountService.setDefault(UUID.fromString(jwt.getSubject()), accountId);
	}
}
