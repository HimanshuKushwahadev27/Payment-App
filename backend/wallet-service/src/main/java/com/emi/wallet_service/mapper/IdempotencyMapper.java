package com.emi.wallet_service.mapper;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.emi.wallet_service.RequestDtos.CreateAccountDto;
import com.emi.wallet_service.entity.IdempotencyKeys;
import com.emi.wallet_service.enums.IdempotencyStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class IdempotencyMapper {

	private final ObjectMapper objectMapper;


	public IdempotencyKeys toEntity(CreateAccountDto request, UUID idempotencyKey, UUID userKeycloakId) {
		IdempotencyKeys idempotency = new IdempotencyKeys();
		String hashedRequest = String.valueOf(request.hashCode());
		
		idempotency.setIdempotencyKey(idempotencyKey);
		idempotency.setCreatedAt(Instant.now());
		idempotency.setUpdatedAt(Instant.now());
		idempotency.setUserKeycloakId(userKeycloakId);
		idempotency.setStatus(IdempotencyStatus.IN_PROGRESS);
		idempotency.setRequestHash(hashedRequest);
		
		return idempotency;
	}

}
