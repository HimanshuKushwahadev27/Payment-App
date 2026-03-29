package com.emi.user_service.DTOs;


public record KycRequest(
  String adhaarNumber,
  String panNumber
) {
  
}
