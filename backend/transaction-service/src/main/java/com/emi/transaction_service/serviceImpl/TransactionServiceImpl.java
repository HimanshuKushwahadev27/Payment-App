package com.emi.transaction_service.serviceImpl;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.emi.events.payment.PaymentEvent;
import com.emi.events.transactions.TransactionEvent;
import com.emi.transaction_service.entity.IdempotencyRecord;
import com.emi.transaction_service.entity.Transaction;
import com.emi.transaction_service.enums.IdempotencyStatus;
import com.emi.transaction_service.exception.TransactionNotFoundException;
import com.emi.transaction_service.exception.UnauthorizedException;
import com.emi.transaction_service.kafka.EventGeneration;
import com.emi.transaction_service.mapper.EventMapper;
import com.emi.transaction_service.mapper.IdempotencyMapper;
import com.emi.transaction_service.mapper.TransactionMapper;
import com.emi.transaction_service.repository.IdempotencyRepository;
import com.emi.transaction_service.repository.TransactionRepository;
import com.emi.transaction_service.requestDtos.TransactionPayoutRequestDto;
import com.emi.transaction_service.responseDtos.TransactionResponseDto;
import com.emi.transaction_service.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{

	private final IdempotencyMapper idempotencyMapper;
	private final IdempotencyRepository idempotencyRepo;
	private final ObjectMapper objectMapper ;
	private final TransactionMapper transactionMapper;
	private final TransactionRepository transactionRepo;
	private final EventGeneration eventGeneration;
	private final EventMapper eventMapper;
	
	@Transactional
	@Override
	public TransactionResponseDto payout(TransactionPayoutRequestDto request, UUID idempotencyKey,
			UUID keycloakId) {
		IdempotencyRecord idempotency = idempotencyMapper.getEntity(request, idempotencyKey, keycloakId);

		try {
			idempotencyRepo.save(idempotency);
		} catch (DataIntegrityViolationException ex) {
			IdempotencyRecord existing = idempotencyRepo.findByUserKeycloakIdAndIdempotencyKey(keycloakId, idempotencyKey)
					.orElseThrow();

			if (existing.getStatus() == IdempotencyStatus.COMPLETED) {
				try {
					return objectMapper.readValue(existing.getResponseBody(), TransactionResponseDto.class);
				} catch (JsonProcessingException e) {
					throw new RuntimeException("Failed to deserialize JSON", e);
				}
			}
			throw new IllegalStateException("Request already in progress");
		}
		
		Transaction transaction = transactionMapper.toEntity(keycloakId, request);
		transactionRepo.save(transaction);
		
		TransactionEvent event = eventMapper.toEntity(transaction);
	    eventGeneration.eventPayout(event);
	    
	    return transactionMapper.toDto(transaction);
	}

	@Override
	public List<TransactionResponseDto> getAll(UUID userKeycloakId) {
		
		List<Transaction> transactions = transactionRepo.findAllByUserKeycloakId(userKeycloakId);
		
		return transactions.stream().map(transactionMapper::toDto).toList();
	}

	@Override
	public TransactionResponseDto get(UUID transactionId, UUID keycloakId) {
		Transaction transaction  = transactionRepo
				.findById(transactionId)
				.orElseThrow(
						() -> new TransactionNotFoundException("not availabe transaction looking for")
						);
		if(!transaction.getUserKeycloakId().equals(keycloakId)) {
			throw new UnauthorizedException("you're not authorized");
		}
		return transactionMapper.toDto(transaction);
	}


	@Override
	public void payoutEventSuccess(TransactionEvent event) {
		Transaction transaction = transactionRepo
				.findById(UUID.fromString((String)event.getTransactionId()))
				.orElseThrow(
						() -> new  TransactionNotFoundException("What u r looking for isn't availabe")
						);
		
	    transaction.setStatus(event.getTransactionStatus());
	    transaction.setUpdatedAt(Instant.now());
	    
	    eventGeneration.eventUpdateLedgerPayout(event);
	}

	@Override
	public void payoutEventFailure(TransactionEvent event) {
		Transaction transaction = transactionRepo
				.findById(UUID.fromString((String)event.getTransactionId()))
				.orElseThrow(
						() -> new  TransactionNotFoundException("What u r looking for isn't availabe")
						);
		
	    transaction.setStatus(event.getTransactionStatus());
	    transaction.setUpdatedAt(Instant.now());
	    
	    eventGeneration.eventUpdateLedgerPayoutFailure(event);
	}

	@Override
	public void paymentEventFailure(PaymentEvent event) {
		Transaction transaction =  transactionMapper.toEntityDepositFailure(event);
		transactionRepo.save(transaction);
		TransactionEvent depositEvent = eventMapper.toEntity(transaction);

		eventGeneration.eventUpdateLedgerDepositFailure(depositEvent);
	}

	@Override
	public void paymentEventSuccess(PaymentEvent event) {
		Transaction transaction =  transactionMapper.toEntityDepositSuccess(event);
		
		transactionRepo.save(transaction);
		
		TransactionEvent depositEvent = eventMapper.toEntity(transaction);
		eventGeneration.eventUpdateLedgerDeposit(depositEvent);
	}
}
