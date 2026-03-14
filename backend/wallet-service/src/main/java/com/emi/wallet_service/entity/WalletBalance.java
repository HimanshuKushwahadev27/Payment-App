package com.emi.wallet_service.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="wallet_balance")
@NoArgsConstructor
@Data
public class WalletBalance {


	
    @Column(name = "account_id", nullable = false, unique=true)
    private UUID accountId;
    
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    
    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;
    
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
