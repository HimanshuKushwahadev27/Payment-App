package com.emi.payment_service.mapper;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.emi.payment_service.RequestDtos.GatewayPaymentRequest;
import com.emi.payment_service.RequestDtos.GatewayPayoutRequest;
import com.emi.payment_service.entity.Payments;
import com.emi.payment_service.enums.GatewayPaymentStatus;

@Component
public class GatewayMapper {


	public BigDecimal convertAmount(Long amount) {
		   if (amount == null) {
		        return BigDecimal.ZERO;
		    }

		    return BigDecimal.valueOf(amount)
		            .divide(BigDecimal.valueOf(100));
	}

	public GatewayPaymentStatus mapStatus(String status) {

		 return switch (status) {
		
		     case "succeeded" -> GatewayPaymentStatus.SUCCESS;
		
		     case "requires_payment_method",
		          "requires_confirmation",
		          "requires_action",
		          "processing" -> GatewayPaymentStatus.PENDING;
		
		     case "canceled" -> GatewayPaymentStatus.FAILED;
		
		     default -> GatewayPaymentStatus.FAILED;
	    };
    }

	public GatewayPaymentRequest getRequest(Payments payment, UUID idempotencyKey, String id) {
		return new GatewayPaymentRequest(
				payment.getAmount(),
				idempotencyKey.toString(),
				payment.getCurrency(),
				id
				);
	}

	public GatewayPayoutRequest getRequestPayout(Payments payment, UUID idempotencyKey, String destinationAccountId) {
		return new GatewayPayoutRequest (
				payment.getUserKeycloakId(),
				payment.getAmount(),
				payment.getCurrency(),
				destinationAccountId,
				idempotencyKey.toString()
				);
	}

}
