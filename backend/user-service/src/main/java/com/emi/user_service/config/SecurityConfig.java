package com.emi.user_service.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import io.minio.MinioClient;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/public/**").permitAll()
                    .anyRequest().authenticated()
			)
			.oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(new KeycloakJwtConverter())))
			.build()
			;
	}
	
 	@Bean
 	OpenAPI openApiConfig() {
 		return new OpenAPI()
 				.info(new Info()
 						.title("User Service API")
 						.description("API documentation for User-service")
 						.version("2.0")
 						.license(new License()
 								.name("Apache 2.0")
 								.url("http://springdoc.org")
 								))
 				.externalDocs(new ExternalDocumentation()
 						.description("Link to external documentation")
 						.url("https://user-demo.com/docs"));
 	}
 	
 	@Bean
 	Keycloak keycloak() {
 	    return KeycloakBuilder.builder()
 	            .serverUrl("http://keycloak:8080")
 	            .realm("Payment-realm")
 	            .clientId("Payment-client")
 	            .username("admin")
 	            .password("admin")
 	            .grantType(OAuth2Constants.PASSWORD)
 	            .build();
 	}

	MinioClient minioClient(){
		return MinioClient.builder()
		 			.endpoint("http://localhost:9000")
					.credentials("admin", "admin")
					.build();
	}
	
}
