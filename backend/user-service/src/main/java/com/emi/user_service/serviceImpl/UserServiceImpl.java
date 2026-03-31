package com.emi.user_service.serviceImpl;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import com.emi.user_service.DTOs.UserRequestCreateDto;
import com.emi.user_service.DTOs.UserRequestUpdateDto;
import com.emi.user_service.DTOs.UserResponseDto;
import com.emi.user_service.entity.UserInfo;
import com.emi.user_service.exceptions.UserExistsException;
import com.emi.user_service.mapper.UserMapper;
import com.emi.user_service.repository.UserRepo;
import com.emi.user_service.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final Keycloak keycloak;
	private final UserMapper userMapper;
	private final UserRepo userRepo;
	private final StripeService stripeService;

	@Override
	public UserResponseDto createUser(UserRequestCreateDto request, UUID keycloakId, String email) {
		if(userRepo.existsByKeycloakId(keycloakId)) {
			throw new UserExistsException("User with they id "+ keycloakId + "is already registered");
		}
		
		var user = userMapper.getEntity(request, keycloakId, email);

		String stripeAccId = stripeService.createConnectAccount(email);
		user.setStripeAccountId(stripeAccId);
		userRepo.save(user);
		
		return userMapper.toDto(user);
	}

	@Override
	public UserResponseDto getUser(UUID keycloakId) {
		
		var user = userRepo.findByKeycloakId(keycloakId)
				.orElseThrow(() -> new UserExistsException("please register urself first"));
		
		if(user.getIsDeleted()) {
			throw new UserExistsException("User is Deleted");
		}
		
		return userMapper.toDto(user);
	}

	@Override
	public List<UserResponseDto> getAllUser() {
		List<UserInfo> users = userRepo.findAll();
		
		return users.stream().map(userMapper::toDto).toList();
	}

	@Override
	public void deleteUser(UUID keycloakId) {
		UserInfo user = userRepo.findByKeycloakId(keycloakId)
				.orElseThrow(() -> new UserExistsException("please register urself first"));
		
		if(user.getIsDeleted()) {
			throw new UserExistsException("User is Deleted");
		}
		
		user.setIsDeleted(true);
		userRepo.save(user);
	}

	@Override
	public UserResponseDto updateUser(UserRequestUpdateDto request, String email, UUID keycloakId) {
		
		UserInfo user = userRepo.findByKeycloakId(keycloakId)
				.orElseThrow(() -> new UserExistsException("please register urself first"));
		
		if(user.getIsDeleted()) {
			throw new UserExistsException("User is Deleted");
		}
		
		userMapper.updateUser(user, email, request);
		userRepo.save(user);
		return userMapper.toDto(user);
	}

	@Override
	public String getEmail(UUID userId) {
		UserRepresentation user= keycloak.realm("wallet-realm")
				.users()
				.get(userId.toString())
				.toRepresentation();
		
		return user.getEmail();
	}

	@Override
	public void saveImageUrl(String imgUrl, UUID keycloakId) {
	UserInfo user = userRepo.findByKeycloakId(keycloakId).orElseThrow(() ->  new UserExistsException("please register ur self"));

	log.info("from userService" + imgUrl);
	user.setProfileImgUrl(imgUrl);
	user.setUpdatedAt(Instant.now());
	userRepo.save(user);
	}

	@Override
	public String getStripeAccountId(UUID keycloakId) {
		return userRepo.findByKeycloakId(keycloakId)
		.map(UserInfo::getStripeAccountId)
		.orElseThrow(() -> new UserExistsException("User not found"));
	}	

}
