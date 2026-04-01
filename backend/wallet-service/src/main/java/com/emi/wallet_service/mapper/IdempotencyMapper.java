package com.emi.wallet_service.mapper;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.emi.wallet_service.RequestDtos.CreateAccountDto;
import com.emi.wallet_service.ResponseDto.PayoutAccountResponse;
import com.emi.wallet_service.entity.IdempotencyKeys;
import com.emi.wallet_service.enums.IdempotencyStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
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



	public void updateIdemp(IdempotencyKeys idempotency, PayoutAccountResponse dto) {
		String responseString = "";
		try {
		 responseString = objectMapper.writeValueAsString(dto);
		}catch(JsonProcessingException  ex) {
			throw new RuntimeException("Failed to serialize response " ,ex);
		}
		idempotency.setHttpStatus(201);
		idempotency.setUpdatedAt(Instant.now());
		idempotency.setExpiresAt(Instant.now().plus(Duration.ofHours(24)));
		idempotency.setResponseBody(responseString);
		idempotency.setStatus(IdempotencyStatus.COMPLETED);
				
	}

}
