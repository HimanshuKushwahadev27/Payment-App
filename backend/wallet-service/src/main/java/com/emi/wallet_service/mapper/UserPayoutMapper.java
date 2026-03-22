package com.emi.wallet_service.mapper;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.emi.wallet_service.RequestDtos.CreatePayoutAccountRequest;
import com.emi.wallet_service.ResponseDto.PayoutAccountResponse;
import com.emi.wallet_service.entity.User_Payout_Account;

@Component
public class UserPayoutMapper {

	public User_Payout_Account toEntity(CreatePayoutAccountRequest request, UUID userKeycloakId) {
		User_Payout_Account user = new User_Payout_Account();
		
		user.setUpdatedAt(Instant.now());
		user.setUserKeycloakId(userKeycloakId);
		user.setBankName(request.bankName());
		user.setCreatedAt(Instant.now());
		user.setDefault(request.isDefault());
		user.setDestinationAccountId(request.destinationAccountId());
		user.setLast4(request.last4());
		return user;
	}

	public PayoutAccountResponse toDto(User_Payout_Account entity) {
		return new PayoutAccountResponse(
				entity.getId(),
				entity.getUserKeycloakId(),
				entity.getDestinationAccountId(),
				entity.getBankName(),
				entity.getLast4(),
				entity.isDefault(),
				entity.getCreatedAt(),
				entity.getUpdatedAt()
				);
	}



}
