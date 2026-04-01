package com.emi.wallet_service.repositories;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.emi.wallet_service.entity.ProcessedEvents;

public interface ProcessedEventRepo extends JpaRepository<ProcessedEvents, UUID>{

	 @Modifying
	 @Query("DELETE FROM ProcessedEvents i WHERE i.expiresAt < :now")
	 void deleteExpired(@Param("now") Instant now);

}
