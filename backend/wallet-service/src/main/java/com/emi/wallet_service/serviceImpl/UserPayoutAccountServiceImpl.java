package com.emi.wallet_service.serviceImpl;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


import com.emi.wallet_service.RequestDtos.CreatePayoutAccountRequest;
import com.emi.wallet_service.RequestDtos.UpdatePayoutAccountRequest;
import com.emi.wallet_service.ResponseDto.PayoutAccountResponse;
import com.emi.wallet_service.entity.IdempotencyKeys;
import com.emi.wallet_service.entity.User_Payout_Account;
import com.emi.wallet_service.enums.IdempotencyStatus;
import com.emi.wallet_service.mapper.IdempotencyMapper;
import com.emi.wallet_service.mapper.UserPayoutMapper;
import com.emi.wallet_service.repositories.IdempotencyRepo;
import com.emi.wallet_service.repositories.UserPayoutRepo;
import com.emi.wallet_service.service.UserPayoutAccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserPayoutAccountServiceImpl implements UserPayoutAccountService{

	private final IdempotencyMapper idempotencyMapper;
	private final IdempotencyRepo idempotencyRepo;
	private final UserPayoutRepo payoutRepo;
	private final UserPayoutMapper userPayoutMapper;
	private final ObjectMapper objectMapper ;

	
	@Override
	public PayoutAccountResponse create(CreatePayoutAccountRequest request,UUID idempotencyKey,  UUID userKeycloakId) {
		
		IdempotencyKeys idempotency = idempotencyMapper.toEntityPayoutAccount(request, idempotencyKey, userKeycloakId);

		try {
			idempotencyRepo.save(idempotency);
		} catch (DataIntegrityViolationException ex) {
			IdempotencyKeys existing = idempotencyRepo.findByUserKeycloakIdAndIdempotencyKey(userKeycloakId, idempotencyKey)
					.orElseThrow();

			if (existing.getStatus() == IdempotencyStatus.COMPLETED) {
				try {
					return objectMapper.readValue(existing.getResponseBody(), PayoutAccountResponse.class);
				} catch (JsonProcessingException e) {
					throw new RuntimeException("Failed to deserialize JSON", e);
				}
			}
			throw new IllegalStateException("Request already in progress");
		}
		
		if(request.isDefault()) {
			payoutRepo.findByUserKeycloakIdAndIsDefaultTrue(userKeycloakId).ifPresent(existing -> {
				existing.setDefault(true);
				payoutRepo.save(existing);
			});
		}
		
        User_Payout_Account entity = userPayoutMapper.toEntity(request, userKeycloakId);

        payoutRepo.save(entity);
        
        idempotencyMapper.updateIdemp(idempotency, userPayoutMapper.toDto(entity));
        idempotencyRepo.save(idempotency);
        
        return userPayoutMapper.toDto(entity);
	}

	@Override
	public List<PayoutAccountResponse> getUsersAccount(UUID userId) {
		return payoutRepo.findByUserKeycloakId(userId)
				.stream().map(userPayoutMapper::toDto).toList();
	}

	@Override
	public PayoutAccountResponse getDefaultAccount(UUID userId) {
		  return payoutRepo.findByUserKeycloakIdAndIsDefaultTrue(userId)
	                .map(userPayoutMapper::toDto)
	                .orElseThrow(() -> new RuntimeException("Default account not found"));
	}

	@Override
	public PayoutAccountResponse update(UUID id, UpdatePayoutAccountRequest request) {
        User_Payout_Account entity = payoutRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));


        if (request.bankName() != null) {
            entity.setBankName(request.bankName());
        }

        if (request.isDefault() != null && request.isDefault()) {
            setDefault(entity.getUserKeycloakId(), entity.getId());
        }

        entity.setUpdatedAt(Instant.now());
        payoutRepo.save(entity);
        
        return userPayoutMapper.toDto(entity);
	}

	@Override
	public void delete(UUID id) {
		payoutRepo.deleteById(id);
	}

	@Override
	public void setDefault(UUID userId, UUID accountId) {
        List<User_Payout_Account> accounts = payoutRepo.findByUserKeycloakId(userId);

        for (User_Payout_Account user : accounts) {
        	user.setDefault(user.getId().equals(accountId));
        }
        
        payoutRepo.saveAll(accounts);
	}

}
