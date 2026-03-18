package com.emi.wallet_service.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emi.wallet_service.ResponseDto.LedgerResponseDto;
import com.emi.wallet_service.service.LedgerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wallet/ledger")
@RequiredArgsConstructor
public class LedgerController {

	private final LedgerService ledgerService;
	
	@GetMapping("/{transactionId}")
	public ResponseEntity<LedgerResponseDto> getBalance(
			@PathVariable UUID transactionId) {
		return ResponseEntity.ok(ledgerService.getUsersRecord(transactionId));
		
	}
	
}
