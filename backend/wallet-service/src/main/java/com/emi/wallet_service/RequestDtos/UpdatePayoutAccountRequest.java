package com.emi.wallet_service.RequestDtos;

public record UpdatePayoutAccountRequest(
	     String bankName,
	        Boolean isDefault
		) {

}
