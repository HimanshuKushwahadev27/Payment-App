package com.emi.user_service.entity;

import java.util.UUID;

import com.emi.user_service.enums.DocumentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="document")
public class Document {
  
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id ;

  @Column(name="img_url")
	private String imgUrl;

	@Column(name="type")
	@Enumerated(EnumType.STRING)
  private DocumentType type;

  @Column(name="keycloak_id", nullable=false)
  private UUID  keycloakId;
}
