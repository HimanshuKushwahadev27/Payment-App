package com.emi.transaction_service.serviceImpl;

import java.time.Instant;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.emi.transaction_service.repository.IdempotencyRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class IdempotencyCleanupJob {

	
		private final IdempotencyRepository idempRepo;
		
		@Transactional
		@Scheduled(fixedRate = 60*60*100)
		public void cleanUp() {
			idempRepo.deleteExpired(Instant.now());
			
			log.info("idempotency cleaned");
		}
}
