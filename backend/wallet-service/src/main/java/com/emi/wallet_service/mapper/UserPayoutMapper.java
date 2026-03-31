package com.emi.wallet_service.mapper;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.emi.wallet_service.ResponseDto.PayoutAccountResponse;
import com.emi.wallet_service.entity.User_Payout_Account;

@Component
public class UserPayoutMapper {


	public PayoutAccountResponse toDto(User_Payout_Account entity, String bankName, String last4) {
		return new PayoutAccountResponse(
				entity.getId(),
				entity.getUserKeycloakId(),
				entity.getStripeAccountId(),
				entity.isChargesEnabled(),
				entity.isPayoutsEnabled(),
				bankName,
				last4,
				entity.getCreatedAt(),
				entity.getUpdatedAt()
				);
	}



}
