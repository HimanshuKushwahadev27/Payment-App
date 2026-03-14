package com.emi.wallet_service.entity;

import java.time.Instant;
import java.util.UUID;

import com.emi.wallet_service.enums.AccountType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

	@Entity
	@Table(
		    name = "account",
		    uniqueConstraints = {
		        @UniqueConstraint(
		            name = "uk_user_account_type",
		            columnNames = {"user_id", "account_type"}
		        )
		    }
		)
	@NoArgsConstructor
	@Data
	public class Account {
	
		@Id
		@GeneratedValue(strategy = GenerationType.UUID)
		private UUID id ;
		
	    @Column(name = "user_id", nullable = false)
	    private UUID userId;
	    
	    @Enumerated(EnumType.STRING)
	    @Column(name = "account_type", nullable = false)
	    private AccountType accountType;
	    
	
	    @Column(name = "created_at", nullable = false)
	    private Instant createdAt;
	    
	    @Column(name = "currency", nullable = false)
	    private String currency ;
	}
