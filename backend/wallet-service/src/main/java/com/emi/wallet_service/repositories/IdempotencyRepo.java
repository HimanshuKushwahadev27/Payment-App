package com.emi.wallet_service.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emi.wallet_service.entity.IdempotencyKeys;

public interface IdempotencyRepo extends JpaRepository<IdempotencyKeys, UUID> {

}
