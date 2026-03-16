package com.emi.wallet_service.mapper;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.emi.wallet_service.RequestDtos.CreateAccountDto;
import com.emi.wallet_service.ResponseDto.ResponseAccountDto;
import com.emi.wallet_service.entity.Account;

@Component
public class AccountMapper {


	public Account toEntity(CreateAccountDto request, UUID userKeycloakId) {
		Account account = new Account();
		
		account.setAccountType(request.type());
		account.setUserKeycloakId(userKeycloakId);
		account.setCreatedAt(Instant.now());
		account.setCurrency(request.currency());
		return account;
	}

	public ResponseAccountDto toDto(Account account) {
		return new ResponseAccountDto(
				account.getId(),
				account.getCreatedAt(),
				account.getCurrency(),
				account.getAccountType()
				);
	}

}
