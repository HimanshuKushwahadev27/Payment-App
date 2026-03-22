package com.emi.wallet_service.RequestDtos;


public record CreatePayoutAccountRequest(
	        String destinationAccountId,
	        String bankName,
	        String last4,
	        boolean isDefault
		) {

}
