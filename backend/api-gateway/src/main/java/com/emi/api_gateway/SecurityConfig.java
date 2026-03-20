package com.emi.api_gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;



import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	private final String [] WHITELIST= {
			"/v3/api-docs/**",
			"/swagger-ui.html",
			"/swagger-ui/**",
			"/actuator/**",
			"/swagger-resources/**",
			"/api-docs/**",
			"/aggregate/**",
			"*/actuator/prometheus"
	};
	
    @Bean
    SecurityWebFilterChain  securityWebFilterChain(ServerHttpSecurity  http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                    .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll() 
                	.pathMatchers(WHITELIST).permitAll()
                    .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                    oauth2.jwt(Customizer.withDefaults())
                )
                .build();
    }
    
   
}  
    
  

   
  