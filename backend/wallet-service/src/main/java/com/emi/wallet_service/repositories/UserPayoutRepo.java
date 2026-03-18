package com.emi.wallet_service.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emi.wallet_service.entity.User_Payout_Account;

public interface UserPayoutRepo extends JpaRepository<User_Payout_Account , UUID> {

	List<User_Payout_Account> findByUserKeycloakId(UUID userId);

	 Optional<User_Payout_Account>  findByUserKeycloakIdAndIsDefaultTrue(UUID userKeycloakId);

}
