package com.emi.wallet_service.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emi.wallet_service.entity.WalletBalance;

public interface WalletBalanceRepo extends JpaRepository<WalletBalance, UUID> {

}
