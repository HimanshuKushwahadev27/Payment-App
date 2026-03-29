package com.emi.user_service.mapper;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.emi.user_service.DTOs.UserRequestCreateDto;
import com.emi.user_service.DTOs.UserRequestUpdateDto;
import com.emi.user_service.DTOs.UserResponseDto;
import com.emi.user_service.entity.UserInfo;

@Component
public class UserMapper {

	public UserInfo getEntity(UserRequestCreateDto request, UUID keycloakId, String email) {
		UserInfo  user = new UserInfo();
		
		user.setCreatedAt(Instant.now());
		user.setEmail(email);
		user.setIsDeleted(false);
		user.setKeycloakId(keycloakId);
		user.setKycStatus(request.kycStatus());
		user.setName(request.name());
		user.setPhone(request.phone());
		user.setUpdatedAt(Instant.now());
		return user;
	}

	public UserResponseDto toDto(UserInfo user) {
		return new UserResponseDto(
				user.getId(),
				user.getName(),
				user.getProfileImgUrl(),
				user.getEmail()	,
				user.getPhone(),
				user.getKycStatus(),
				user.getCreatedAt()
				);
	}

	public void updateUser(UserInfo user, String email, UserRequestUpdateDto request) {
		user.setUpdatedAt(Instant.now());
		user.setEmail(email);

		if(request.name()!=null){
		 user.setName(request.name());
		}

		if(request.phone()!=null){
			user.setPhone(request.phone());
		}
	}

}
