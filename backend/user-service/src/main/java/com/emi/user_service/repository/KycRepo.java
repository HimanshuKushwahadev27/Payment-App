package com.emi.user_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emi.user_service.entity.Kyc;

public interface KycRepo extends JpaRepository<Kyc, UUID> {

  public Kyc findByKeycloakId(UUID keycloakId);

}

