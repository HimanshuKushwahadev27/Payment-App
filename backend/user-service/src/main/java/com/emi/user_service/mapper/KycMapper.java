package com.emi.user_service.mapper;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.emi.user_service.DTOs.KycRequest;
import com.emi.user_service.entity.Kyc;
import com.emi.user_service.enums.KYCSTATUS;

@Component
public class KycMapper {
  
  public Kyc getEntity(KycRequest request, UUID keycloakId){
    
    Kyc kyc = new Kyc();
    kyc.setAadhaarNumber(request.adhaarNumber());
    kyc.setCreatedAt(Instant.now());
    kyc.setOtpVerified(false);
    kyc.setPanNumber(request.panNumber());
    kyc.setStatus(KYCSTATUS.PENDING);
    kyc.setUpdateAt(Instant.now());
    kyc.setKeycloakId(keycloakId);

    return kyc;
  }
}
