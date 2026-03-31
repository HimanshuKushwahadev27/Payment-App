package com.emi.user_service.service;

import java.util.List;
import java.util.UUID;


import com.emi.user_service.DTOs.UserRequestCreateDto;
import com.emi.user_service.DTOs.UserRequestUpdateDto;
import com.emi.user_service.DTOs.UserResponseDto;

public interface UserService {

	public UserResponseDto createUser(UserRequestCreateDto request, UUID keycloakId, String email);
	public UserResponseDto getUser(UUID keycloakId);
	public List<UserResponseDto> getAllUser();
	public void deleteUser(UUID keycloakId);
	public UserResponseDto updateUser(UserRequestUpdateDto request, String email, UUID keycloakId);
	public String getEmail(UUID userId);
	public void saveImageUrl(String imgUrl, UUID keycloakId);
  public String getStripeAccountId(UUID keycloakId);

}
