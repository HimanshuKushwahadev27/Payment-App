package com.emi.user_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emi.user_service.entity.UserInfo;

public interface UserRepo extends JpaRepository<UserInfo, UUID> {

	boolean existsByKeycloakId(UUID keycloakId);

	Optional<UserInfo> findByKeycloakId(UUID keycloakId);

}
