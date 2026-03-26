package com.emi.user_service.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emi.user_service.DTOs.RequestDocument;
import com.emi.user_service.serviceImpl.FileService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user/file")
@RequiredArgsConstructor
public class FileController {
  
  private final FileService fileService;

  @GetMapping("/upload-url")
	public ResponseEntity<String> getUploadUrl(@RequestParam("fileName") String fileName) {
			try {
					String url = fileService.getPresignedUrl(fileName);
					return ResponseEntity.ok(url);
			} catch (Exception e) {
					 e.printStackTrace();   
					return ResponseEntity.internalServerError().build();
			}
	}

	@PostMapping("/save-url")
	public ResponseEntity<String> saveDocument(@RequestBody RequestDocument request, @AuthenticationPrincipal Jwt jwt){
		fileService.storeDocument(request, UUID.fromString(jwt.getSubject()));

		return ResponseEntity.ok("saved !!");
	}
}
