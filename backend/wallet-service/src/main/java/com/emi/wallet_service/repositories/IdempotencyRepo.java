package com.emi.wallet_service.repositories;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.emi.wallet_service.entity.IdempotencyKeys;

public interface IdempotencyRepo extends JpaRepository<IdempotencyKeys, UUID> {

	 @Modifying
	 @Query("DELETE FROM IdempotencyKeys i WHERE i.expiresAt < :now")
	 void deleteExpired(@Param("now")Instant now);

	 Optional<IdempotencyKeys> findByUserKeycloakIdAndIdempotencyKey(UUID userKeycloakId, UUID idempotencyKey);

}
