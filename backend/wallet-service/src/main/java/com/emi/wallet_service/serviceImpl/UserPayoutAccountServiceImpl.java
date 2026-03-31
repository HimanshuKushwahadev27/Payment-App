package com.emi.wallet_service.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.emi.wallet_service.RequestDtos.BankTokenRequest;
import com.emi.wallet_service.RequestDtos.CreatePayoutAccountRequest;
import com.emi.wallet_service.ResponseDto.PayoutAccountResponse;
import com.emi.wallet_service.client.UserClient;
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
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.BankAccount;
import com.stripe.model.ExternalAccount;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserPayoutAccountServiceImpl implements UserPayoutAccountService{

	private final IdempotencyMapper idempotencyMapper;
	private final IdempotencyRepo idempotencyRepo;
	private final UserPayoutRepo payoutRepo;
	private final UserPayoutMapper userPayoutMapper;
	private final ObjectMapper objectMapper ;
	private final UserClient userClient;

	@Value("${stripe.secret.key}")
	private String stripeSecretKey;
	
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


	@Override
	public PayoutAccountResponse storeBankDetails(BankTokenRequest request, UUID idempotencyKey, UUID userId) {

			Stripe.apiKey = stripeSecretKey;

		try{

			String 	accId = userClient.getEmail(userId);

			Map<String, Object> params = new HashMap<>();
				params.put("object", "bank_account");
        params.put("country", "IN");
        params.put("currency", "inr");
        params.put("account_holder_name", request.accountHolderName());
        params.put("account_holder_type", "individual");
        params.put("routing_number", request.ifscCode()); 
        params.put("account_number", request.accountNumber());

				Map<String, Object> externalAccountParams = new HashMap<>();
        externalAccountParams.put("external_account", params);

				ExternalAccount externalAccount = Account.retrieve(accId).getExternalAccounts().create(externalAccountParams);

				BankAccount bankAccount = (BankAccount)externalAccount;

				  CreatePayoutAccountRequest payoutRequest =  new CreatePayoutAccountRequest(
						bankAccount.getId(),
						bankAccount.getBankName(),
						bankAccount.getLast4(),
						request.isDefault()
					);

			return create(payoutRequest, idempotencyKey, userId);

		}catch(StripeException ex){
			throw new RuntimeException("Stripe error: " + ex.getMessage());
		}
	 }
}
