package com.emi.user_service.entity;

import java.time.Instant;
import java.util.UUID;

import com.emi.user_service.enums.KYCSTATUS;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "kyc")
@Data
public class Kyc {
  
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id ;

	@Column(name="keycloak_id", unique=true, nullable=true)
  private UUID keycloakId;

  @Column(name="aadhaar_number", unique=true, nullable=true)
  private String aadhaarNumber;

  @Column(name="pan_number", unique=true, nullable=true)
  private String panNumber;

  @Column(name="document_url")
  private String documentUrl;

  @Column(name="selfie_url")
  private String selfieUrl;

  
	@Column(name="status")
	@Enumerated(EnumType.STRING)
  private KYCSTATUS status;

  @Column(name="otp_verified")
  private boolean otpVerified;

  @Column(name="created_at")
  private Instant createdAt;

  @Column(name="updated_at")
  private Instant updateAt;
}
