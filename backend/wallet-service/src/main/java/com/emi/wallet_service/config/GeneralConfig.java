package com.emi.wallet_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class GeneralConfig {

	
	private final ConcurrentKafkaListenerContainerFactory<?, ?> concurrentKafkaListenerContainerFactory;
	
	@PostConstruct
	public void setObservationForKafkaTemplate() {
		concurrentKafkaListenerContainerFactory.getContainerProperties().setObservationEnabled(true);
	}
	
	
     @Bean
     ObjectMapper objectMapper() {
        return new ObjectMapper();
     }

}
