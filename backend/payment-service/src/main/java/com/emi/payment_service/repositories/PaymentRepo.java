package com.emi.payment_service.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emi.payment_service.entity.Payments;

public interface PaymentRepo extends JpaRepository<Payments, UUID> {

	Payments findByGatewayTransactionId(String transactionId);

}
