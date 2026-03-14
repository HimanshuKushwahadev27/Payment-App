package com.emi.user_service.serviceImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.emi.user_service.DTOs.UserRequestCreateDto;
import com.emi.user_service.DTOs.UserRequestUpdateDto;
import com.emi.user_service.DTOs.UserResponseDto;
import com.emi.user_service.entity.UserInfo;
import com.emi.user_service.exceptions.UserExistsException;
import com.emi.user_service.mapper.UserMapper;
import com.emi.user_service.repository.UserRepo;
import com.emi.user_service.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;
	private final UserRepo userRepo;
	
	@Override
	public UserResponseDto createUser(UserRequestCreateDto request, UUID keycloakId, String email) {
		if(userRepo.existsByKeycloakId(keycloakId)) {
			throw new UserExistsException("User with they id "+ keycloakId + "is already registered");
		}
		
		var user = userMapper.getEntity(request, keycloakId, email);
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

}
