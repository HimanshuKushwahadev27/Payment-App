package com.emi.user_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emi.user_service.DTOs.UserRequestCreateDto;
import com.emi.user_service.DTOs.UserRequestUpdateDto;
import com.emi.user_service.DTOs.UserResponseDto;
import com.emi.user_service.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	@PostMapping("/create")
	public ResponseEntity<UserResponseDto> createUser(
			@RequestBody @Valid UserRequestCreateDto request,
			@AuthenticationPrincipal Jwt jwt){
		return ResponseEntity.ok(userService
				.createUser(
						request,
						UUID.fromString(jwt.getSubject()),
						jwt.getClaimAsString("email")
						)
				);
	}
	
	@GetMapping("/")
	public ResponseEntity<UserResponseDto> getUser(@AuthenticationPrincipal Jwt jwt){
		
		return ResponseEntity.ok(userService.getUser(UUID.fromString(jwt.getSubject())));
	}
	
	
	@GetMapping("/getAll")
	public ResponseEntity<List<UserResponseDto>> getAllUser(@AuthenticationPrincipal Jwt jwt){
		
		return ResponseEntity.ok(userService.getAllUser());
	}
	
	@DeleteMapping("/delete")
	public void deleteUser(@AuthenticationPrincipal Jwt jwt) {
		userService.deleteUser(UUID.fromString(jwt.getSubject()));
	}
	
	@PatchMapping("/update")
	public ResponseEntity<UserResponseDto> update(
			@RequestBody @Valid UserRequestUpdateDto request ,
			@AuthenticationPrincipal Jwt jwt){
		return ResponseEntity.ok(userService
				.updateUser(
						request,
						jwt.getClaimAsString("email"),
						UUID.fromString(jwt.getSubject())		
						)
				); 
	}
	
	
	@GetMapping("/users/{id}/email")
	public String getEmail(@PathVariable UUID id) {
	    return userService.getEmail(id);
	}
}

