package com.emi.wallet_service.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="idempotency_keys")
@NoArgsConstructor
@Data
public class IdempotencyKeys {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id ;
	
	@Column(name="idempotency_key", unique=true, nullable=false)
	private UUID idempotencyKey;
	
	@Column(name="request_hash")
	private String requestHash;
	
	@Column(name="response_body")
	private String responseBody;
	
	@Column(name="created_at", nullable=false)
	private Instant createdAt;
}
