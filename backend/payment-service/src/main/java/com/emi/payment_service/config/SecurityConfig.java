package com.emi.payment_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
	
	private final ConcurrentKafkaListenerContainerFactory<?, ?> concurrentKafkaListenerContainerFactory;
	
	@PostConstruct
	public void setObservationForKafkaTemplate() {
		concurrentKafkaListenerContainerFactory.getContainerProperties().setObservationEnabled(true);
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/public/**").permitAll()
                    .anyRequest().authenticated()
			)
			.oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))
			.build()
			;
	}
	
	
    @Bean
     ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    
    
 	@Bean
 	OpenAPI openApiConfig() {
 		return new OpenAPI()
 				.info(new Info()
 						.title("Payment Service API")
 						.description("API documentation for Payment-service")
 						.version("2.0")
 						.license(new License()
 								.name("Apache 2.0")
 								.url("http://springdoc.org")
 								))
 				.externalDocs(new ExternalDocumentation()
 						.description("Link to external documentation")
 						.url("https://payment-demo.com/docs"));
 	}
}
