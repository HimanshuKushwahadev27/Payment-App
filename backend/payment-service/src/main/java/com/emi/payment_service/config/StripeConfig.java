package com.emi.payment_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class StripeConfig {

	@Value("${stripe.secret.key}")
	private String secretKey;
	
	@Bean
	WebClient stripeClient(WebClient.Builder builder) {
		return builder
				.baseUrl("https://api.stripe.com")
				.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer ", secretKey)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.build()
				;
	}
	
	@Bean
	WebClient.Builder webClientBuilder() {
			return WebClient.builder();
	}
}
