package com.emi.user_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emi.user_service.entity.Document;

public interface DocumentRepo extends JpaRepository<Document, UUID>{
  
}
