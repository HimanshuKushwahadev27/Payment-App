package com.emi.payment_service.gatewayPayment;

import com.emi.payment_service.RequestDtos.GatewayPaymentRequest;
import com.emi.payment_service.RequestDtos.GatewayPayoutRequest;
import com.emi.payment_service.RequestDtos.GatewayRefundRequest;
import com.emi.payment_service.ResponseDtos.GatewayResponse;

public interface PaymentGateway {
	
    GatewayResponse charge(GatewayPaymentRequest request);

    GatewayResponse refundPayment(GatewayRefundRequest request);

    GatewayResponse cancelPayment(String transactionId);
    
    GatewayResponse payout(GatewayPayoutRequest request);

}
