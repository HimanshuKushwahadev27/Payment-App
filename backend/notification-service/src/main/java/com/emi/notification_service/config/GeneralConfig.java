package com.emi.notification_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class GeneralConfig {

	private final ConcurrentKafkaListenerContainerFactory<?, ?> concurrentKafkaListenerContainerFactory;
	
	@PostConstruct
	public void setObservationForKafkaTemplate() {
		concurrentKafkaListenerContainerFactory.getContainerProperties().setObservationEnabled(true);
	}
}
