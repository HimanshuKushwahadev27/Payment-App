package com.emi.transaction_service.mapper;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.emi.transaction_service.entity.IdempotencyRecord;
import com.emi.transaction_service.enums.IdempotencyStatus;
import com.emi.transaction_service.requestDtos.TransactionPayoutRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class IdempotencyMapper {

	private final ObjectMapper objectMapper;

	
	public IdempotencyRecord getEntity(TransactionPayoutRequestDto request, UUID idempotencyId, UUID keycloakId) {
		IdempotencyRecord idempotency = new IdempotencyRecord();
		String hashedRequest = String.valueOf(request.hashCode());
		
		idempotency.setIdempotencyKey(idempotencyId);
		idempotency.setCreatedAt(Instant.now());
		idempotency.setUpdatedAt(Instant.now());
		idempotency.setUserKeycloakId(keycloakId);
		idempotency.setStatus(IdempotencyStatus.IN_PROGRESS);
		idempotency.setRequestHash(hashedRequest);
		
		return idempotency;
	}
}
