package com.emi.user_service.serviceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StripeService {
  
  
	@Value("${stripe.secret.key}")
	private String stripeSecretKey;

  public String createConnectAccount(String email){
    Stripe.apiKey = stripeSecretKey;

    try{
        Map<String, Object> params = new HashMap<>();
        params.put("type", "custom");
        params.put("country", "US");
        params.put("email", email);
        params.put("capabilities", Map.of(
            "transfers", Map.of("requested", true)
        ));


        Account account = Account.create(params);
        log.info("Stripe Connect account created: {}", account.getId());
        return account.getId();
    }catch(StripeException ex){
        log.error("Failed to create Stripe Connect account: {}", ex);

        throw new RuntimeException("Stripe account creation failed: " + ex.getMessage());
    }
  }
}
