package com.emi.wallet_service.RequestDtos;

import jakarta.validation.constraints.NotBlank;

public record BankTokenRequest(

    @NotBlank
     String accountHolderName,
    @NotBlank
     String ifscCode,
    @NotBlank
     String accountNumber,
     boolean isDefault

) {
  
}
