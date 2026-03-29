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

import com.emi.user_service.DTOs.KycRequest;
import com.emi.user_service.serviceImpl.KycService;
import com.emi.user_service.serviceImpl.OtpService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user/kyc")
@RequiredArgsConstructor
public class KycController {
  
  private final OtpService otpService;
  private final KycService kycService;

  	@PostMapping("/submit")
	public ResponseEntity<String> createUser(
			@RequestBody @Valid KycRequest request,
			@AuthenticationPrincipal Jwt jwt){
		return ResponseEntity.ok(kycService
				.completeKyc(
						request,
						UUID.fromString(jwt.getSubject())
						)
				);
	}

    @PostMapping("/otp/send")
  public String sendOtp(@RequestParam("phone") Long phone) {
      otpService.sendOtp(phone);
      return "OTP sent successfully";
  }

  @PostMapping("/otp/verify")
  public String verifyOtp(
    @AuthenticationPrincipal Jwt jwt,
    @RequestParam("phone") Long phone,
    @RequestParam("otp") String otp) {

      otpService.verifyOtp(UUID.fromString(jwt.getSubject()), phone, otp);
      return "OTP verified successfully";
  }
}
