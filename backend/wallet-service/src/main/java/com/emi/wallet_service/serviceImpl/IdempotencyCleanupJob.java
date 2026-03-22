package com.emi.wallet_service.serviceImpl;

import java.time.Instant;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.emi.wallet_service.repositories.IdempotencyRepo;
import com.emi.wallet_service.repositories.ProcessedEventRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class IdempotencyCleanupJob {

	
		private final IdempotencyRepo idempRepo;
		private final ProcessedEventRepo eventRepo;
		@Transactional
		@Scheduled(fixedRate = 60*60*100)
		public void cleanUp() {
			idempRepo.deleteExpired(Instant.now());
			
			log.info("idempotency cleaned");
		}
		
		@Transactional
		@Scheduled(fixedRate = 60*60*100)
		public void cleanUpProcessed() {
			eventRepo.deleteExpired(Instant.now());
			
			log.info("Processed event  cleaned");
		}
		
		
}
