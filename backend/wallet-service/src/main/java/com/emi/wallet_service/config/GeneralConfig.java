package com.emi.wallet_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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
     
     
     
     
 	
 	@Bean
 	OpenAPI openApiConfig() {
 		return new OpenAPI()
 				.info(new Info()
 						.title("Wallet Service API")
 						.description("API documentation for Wallet-service")
 						.version("2.0")
 						.license(new License()
 								.name("Apache 2.0")
 								.url("http://springdoc.org")
 								))
 				.externalDocs(new ExternalDocumentation()
 						.description("Link to external documentation")
 						.url("https://wallet-demo.com/docs"));
 	}

	

}
