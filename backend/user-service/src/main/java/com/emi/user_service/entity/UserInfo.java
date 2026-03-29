package com.emi.user_service.entity;

import java.time.Instant;
import java.util.UUID;

import com.emi.user_service.enums.KYCSTATUS;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="user_info")
public class UserInfo {

	@Id
	@GeneratedValue(strategy=GenerationType.UUID)
	private UUID id;
			
	@Column(name="keycloak_id", unique=true, nullable=false)
	private UUID keycloakId ;
	
	@Column(name="profile_image_url")
	private String profileImgUrl;		
	
	@Column(name="name", nullable=false)
	private String name ;
	
	@Column(name="email", unique=true, nullable=false)
	private String email ;
	
	@Column(name="phone", unique=true, nullable=false)
	private Long phone ;
	
	@Column(name="kyc_status")
	@Enumerated(EnumType.STRING)
	private KYCSTATUS kycStatus ;
	
	@Column(name="created_at", nullable=false)
	private Instant createdAt ;
	
	@Column(name="updated_at", nullable=false)
	private Instant updatedAt ;
	
	@Column(name="is_deleted")
	private Boolean isDeleted;
}
