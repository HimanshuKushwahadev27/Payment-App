package com.emi.wallet_service.service;

import java.util.List;
import java.util.UUID;

import com.emi.wallet_service.RequestDtos.CreatePayoutAccountRequest;
import com.emi.wallet_service.RequestDtos.UpdatePayoutAccountRequest;
import com.emi.wallet_service.ResponseDto.PayoutAccountResponse;

public interface UserPayoutAccountService {

	PayoutAccountResponse create(CreatePayoutAccountRequest request,UUID idempotencyKey,  UUID userId);
	
	List<PayoutAccountResponse> getUsersAccount(UUID userId);
	
    PayoutAccountResponse getDefaultAccount(UUID userId);

    PayoutAccountResponse update(UUID id, UpdatePayoutAccountRequest request);
    
    
    void delete(UUID id);

    void setDefault(UUID userId, UUID accountId);
}
