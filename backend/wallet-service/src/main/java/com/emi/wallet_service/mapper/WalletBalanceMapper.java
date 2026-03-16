package com.emi.wallet_service.mapper;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.stereotype.Component;

import com.emi.wallet_service.entity.Account;
import com.emi.wallet_service.entity.WalletBalance;

@Component
public class WalletBalanceMapper {


	public WalletBalance toEntity(Account account) {
		WalletBalance balance = new WalletBalance();
		
		balance.setAccountId(account.getId());
		balance.setBalance(new BigDecimal("0"));
		balance.setCreatedAt(Instant.now());
		balance.setUpdatedAt(Instant.now());
		return balance ;
	}

}
