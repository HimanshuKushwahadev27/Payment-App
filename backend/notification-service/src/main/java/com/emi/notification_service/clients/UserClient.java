package com.emi.notification_service.clients;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "user-service", url = "http://user-service:8080")
public interface UserClient {

	@GetMapping("api/user/users/{id}/email")
	public String getEmail(@PathVariable UUID id);
}
