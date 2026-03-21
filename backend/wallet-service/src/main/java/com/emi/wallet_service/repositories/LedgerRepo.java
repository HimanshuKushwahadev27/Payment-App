package com.emi.wallet_service.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emi.wallet_service.entity.LedgerEntry;

public interface LedgerRepo extends JpaRepository<LedgerEntry, UUID> {

	Optional<LedgerEntry> findByTransactionId(UUID transaction_Id);


}
