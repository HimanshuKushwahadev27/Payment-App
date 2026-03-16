package com.emi.wallet_service.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emi.wallet_service.entity.User_Payout_Account;

public interface UserPayoutRepo extends JpaRepository<User_Payout_Account , UUID> {

}
