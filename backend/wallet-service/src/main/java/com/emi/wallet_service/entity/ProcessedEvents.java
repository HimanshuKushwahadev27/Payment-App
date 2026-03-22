package com.emi.wallet_service.entity;


import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="processed_events")
@NoArgsConstructor
@Data
public class ProcessedEvents {

	@Id
	private UUID id ;
	
    @Column(name = "expires_at")
	private Instant expiresAt;
}
