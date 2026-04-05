package com.emi.payment_service.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emi.payment_service.RequestDtos.RequestPaymentDto;
import com.emi.payment_service.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
	
	private final PaymentService paymentService;
	
	@PostMapping("/charge")
	public ResponseEntity<Map<String, String>> charge(
			@RequestBody @Valid RequestPaymentDto request,
			@AuthenticationPrincipal Jwt jwt,
			@RequestHeader("Idempotency-Key") String IdempotencyKey){
		
		String clientId = 
				paymentService.createIntent(
						request,
						UUID.fromString(IdempotencyKey), 
						UUID.fromString(jwt.getSubject())
						);
			return ResponseEntity.ok(Map.of("clientSecret", clientId ));
	}
	
	@PostMapping("/webhook")
	public ResponseEntity<Void> webhook(
	        @RequestBody String payload,
	        @RequestHeader("Stripe-Signature") String sigHeader) {
		paymentService.handleWebhook(payload, sigHeader);
	    return ResponseEntity.ok().build();
	}
}
