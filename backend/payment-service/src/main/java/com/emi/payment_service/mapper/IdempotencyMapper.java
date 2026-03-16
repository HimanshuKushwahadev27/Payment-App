package com.emi.payment_service.mapper;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.emi.payment_service.RequestDtos.RequestPaymentDto;
import com.emi.payment_service.ResponseDtos.ResponsePaymentDto;
import com.emi.payment_service.entity.IdempotencyRecord;
import com.emi.payment_service.enums.IdempotencyStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class IdempotencyMapper {

	private final ObjectMapper objectMapper;

	
	public IdempotencyRecord getEntity(RequestPaymentDto request, UUID idempotencyId, UUID keycloakId) {
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
	
	public void updateIdemp(IdempotencyRecord idempotency, ResponsePaymentDto response) {
		String responseString = "";
		try {
		 responseString = objectMapper.writeValueAsString(response);
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