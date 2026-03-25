package com.emi.user_service.DTOs;

import com.emi.user_service.enums.KYCSTATUS;

public record KycRequest(
  String adhaarNumber,
  String panNumber,
  KYCSTATUS status
) {
  
}
