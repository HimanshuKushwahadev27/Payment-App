package com.emi.payment_service.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FeignAuthInterceptor implements RequestInterceptor {

  @Override
  public void apply(RequestTemplate arg0) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            log.info("Feign intercepteion triggered");
            log.info("AUTH " + authentication);
            String token = jwtAuth.getToken().getTokenValue();
            arg0.header("Authorization", "Bearer " + token);
        }
  }

}