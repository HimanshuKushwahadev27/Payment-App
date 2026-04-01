package com.emi.wallet_service.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "user_payout_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User_Payout_Account {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_keycloak_id", nullable = false)
    private UUID userKeycloakId;


    @Column(name = "stripe_account_id", nullable = false, unique = true)
    private String stripeAccountId;

    @Column(name = "charges_enabled")
    private boolean chargesEnabled;

    @Column(name = "payouts_enabled")
    private boolean payoutsEnabled;

    @Column(name = "details_submitted")
    private boolean detailsSubmitted;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

}
