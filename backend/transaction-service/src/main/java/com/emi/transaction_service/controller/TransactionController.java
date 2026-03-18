package com.emi.transaction_service.controller;

import java.util.List;
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

import com.emi.transaction_service.requestDtos.TransactionPayoutRequestDto;
import com.emi.transaction_service.responseDtos.TransactionResponseDto;
import com.emi.transaction_service.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {
	
	private final TransactionService transactionService;
	
	@PostMapping("/payout")
	public ResponseEntity<TransactionResponseDto> payout(
			@RequestBody @Valid TransactionPayoutRequestDto request,
			@AuthenticationPrincipal Jwt jwt,
			@RequestHeader("Idempotency-Key") String IdempotencyKey){
		
		return ResponseEntity.ok(
				transactionService.payout(
						request,
						UUID.fromString(IdempotencyKey), 
						UUID.fromString(jwt.getSubject())
						)
				);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<TransactionResponseDto>> getAll(@AuthenticationPrincipal Jwt jwt){
		return ResponseEntity.ok(transactionService.getAll(UUID.fromString(jwt.getSubject())));
	}
	
	@GetMapping("/get")
	public ResponseEntity<TransactionResponseDto> get(
			@AuthenticationPrincipal Jwt jwt,
			@PathVariable UUID transactionId){
		return ResponseEntity.ok(transactionService.get(
				UUID.fromString(jwt.getSubject()),
				transactionId));
	}
	
	
}
