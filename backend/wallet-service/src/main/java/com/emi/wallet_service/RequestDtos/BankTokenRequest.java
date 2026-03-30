package com.emi.wallet_service.RequestDtos;

import jakarta.validation.constraints.NotNull;

public record BankTokenRequest(

   @NotNull
   String token,
   boolean isDefault

) {
  
}
