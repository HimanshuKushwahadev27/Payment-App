package com.emi.user_service.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.emi.user_service.DTOs.RequestDocument;
import com.emi.user_service.serviceImpl.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user/file")
@RequiredArgsConstructor
public class FileController {
  
  private final FileService fileService;

  @PostMapping("/upload-url")
	public ResponseEntity<String> getUploadUrl(@RequestParam("fileName") MultipartFile  fileName)throws Exception {
		String fileUrl = fileService.uploadFile(fileName);
    return ResponseEntity.ok(fileUrl);
	}

	@PostMapping("/save-url")
	public ResponseEntity<String> saveDocument(@RequestBody RequestDocument request, @AuthenticationPrincipal Jwt jwt){
		fileService.storeDocument(request, UUID.fromString(jwt.getSubject()));

		return ResponseEntity.ok("saved !!");
	}
}
