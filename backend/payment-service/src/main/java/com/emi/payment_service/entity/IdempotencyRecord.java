package com.emi.payment_service.entity;

import java.time.Instant;
import java.util.UUID;

import com.emi.payment_service.enums.IdempotencyStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(
    name = "idempotency_records",
    uniqueConstraints = @UniqueConstraint(columnNames = "idempotency_key")
)
@Data
public class IdempotencyRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	
    @Column(name = "idempotency_key", nullable = false)
	private UUID idempotencyKey;
	
    @Column(name = "user_keycloak_id", nullable = false)
	private UUID userKeycloakId;
	
    @Column(name = "request_hash")
	private String requestHash;
	
    @Column(name = "response_body", columnDefinition = "TEXT")
	private String responseBody;
	
    @Column(name = "http_status")
	private Integer httpStatus;
	
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
	private IdempotencyStatus status;
	
    @Column(name = "created_at", nullable = false, updatable=false)
	private Instant createdAt;
	
    @Column(name = "updated_at")
	private Instant updatedAt;
    
    @Column(name = "expires_at")
	private Instant expiresAt;
}

