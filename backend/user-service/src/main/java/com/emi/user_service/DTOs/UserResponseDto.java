package com.emi.user_service.DTOs;

import java.time.Instant;
import java.util.UUID;

import com.emi.user_service.enums.KYCSTATUS;

public record UserResponseDto(
		UUID id,
		String name,
		String profileImgUrl,
		String email,
	  String stripeAccountId,
		Long phone,
		KYCSTATUS status,
		Instant createdAt
		) {

}
