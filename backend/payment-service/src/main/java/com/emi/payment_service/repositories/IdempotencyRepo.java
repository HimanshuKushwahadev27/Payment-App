package com.emi.payment_service.repositories;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.emi.payment_service.entity.IdempotencyRecord;

public interface IdempotencyRepo extends JpaRepository<IdempotencyRecord, UUID> {

	 @Modifying
	 @Query("DELETE FROM IdempotencyRecord i WHERE i.expiresAt < :now")
	 void deleteExpired(Instant now);

	 Optional<IdempotencyRecord> findByUserKeycloakIdAndIdempotencyKey(UUID keycloakId, UUID idempotencyKey);
	 

}
