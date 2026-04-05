package com.emi.payment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.emi.payment_service.ResponseDtos.ResponseBalanceDto;

@FeignClient(value = "wallet-service", url = "http://wallet-service:8080")
public interface WalletClient {
  

  	@GetMapping("/api/wallet/account/")
  	public ResponseBalanceDto getBalance();
}
