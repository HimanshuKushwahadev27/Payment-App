package com.emi.wallet_service.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emi.wallet_service.RequestDtos.CreateAccountDto;
import com.emi.wallet_service.RequestDtos.TransferRequestDto;
import com.emi.wallet_service.ResponseDto.ReponseBalanceDto;
import com.emi.wallet_service.ResponseDto.ResponseAccountDto;
import com.emi.wallet_service.service.WalletService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wallet/account")
@RequiredArgsConstructor
public class WalletController {

	private final WalletService walletService;
	
	@PostMapping("/create")
	public ResponseEntity<ResponseAccountDto> createAccount(
			@RequestBody @Valid CreateAccountDto request,
			@AuthenticationPrincipal Jwt jwt,
			@RequestHeader("Idempotency-key") UUID idempotencyKey) {
		return ResponseEntity.ok(walletService
				.createAccount(request,
							   idempotencyKey,
							   UUID.fromString(jwt.getSubject())
							)
				);
	}

	
	@PostMapping("/transfer")
	public void transfer(
			@RequestBody @Valid TransferRequestDto request,
			@AuthenticationPrincipal Jwt jwt) {
		walletService.transfer(request, UUID.fromString(jwt.getSubject()));
	}

	
	@GetMapping("/{accountId}")
	public ResponseEntity<ReponseBalanceDto> getBalance(
			@PathVariable UUID accountId,
			@AuthenticationPrincipal Jwt jwt) {
		return ResponseEntity.ok(walletService.getBalance(accountId, UUID.fromString(jwt.getSubject())));
		
	}

}


