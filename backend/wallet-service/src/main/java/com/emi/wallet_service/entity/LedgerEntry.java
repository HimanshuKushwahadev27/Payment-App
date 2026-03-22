package com.emi.wallet_service.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.emi.events.payment.PaymentStatus;
import com.emi.wallet_service.enums.EntryType;

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

@Entity
@Table(name="ledger_entry")
@NoArgsConstructor
@Data
public class LedgerEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id ;
	
    @Column(name = "transaction_id", nullable = false)
    private UUID transactionId;

    @Column(name = "to_account_id", nullable = false)
    private String toAccountId;
    
    @Column(name = "from_account_id", nullable = false)
    private String fromAccountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "entry_type", nullable = false)
    private EntryType entryType;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    
    
	@Column(name = "status", nullable = false, unique = false)
	@Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
