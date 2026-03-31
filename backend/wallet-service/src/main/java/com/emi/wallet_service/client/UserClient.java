package com.emi.wallet_service.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "user-service", url = "http://user-service:8080")
public interface UserClient {
  
  
	@GetMapping("/api/user/stripe-account/{keycloakId}")
	public String getStripeId(@PathVariable("keycloakId") UUID keycloakId);

}
