package com.emi.transaction_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emi.transaction_service.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction , UUID> {

	List<Transaction> findAllByUserKeycloakId(UUID userKeycloakId);

}
