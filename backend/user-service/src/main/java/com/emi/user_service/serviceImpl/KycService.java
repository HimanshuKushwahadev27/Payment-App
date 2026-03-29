package com.emi.user_service.serviceImpl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.emi.user_service.DTOs.KycRequest;
import com.emi.user_service.entity.Kyc;
import com.emi.user_service.entity.UserInfo;
import com.emi.user_service.enums.DocumentType;
import com.emi.user_service.enums.KYCSTATUS;
import com.emi.user_service.exceptions.UserExistsException;
import com.emi.user_service.mapper.KycMapper;
import com.emi.user_service.repository.KycRepo;
import com.emi.user_service.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KycService {

  private final KycMapper kycMapper;
  private final UserRepo userRepo;
  private final KycRepo kycRepo;

  public String completeKyc(KycRequest request, UUID keycloakId){
    if(!userRepo.existsByKeycloakId(keycloakId)){
      throw new UserExistsException("Please register urself first");
    }

    if ((request.panNumber() == null || request.panNumber().isEmpty()) &&
    (request.adhaarNumber() == null || request.adhaarNumber().isEmpty())) {
       throw new RuntimeException("Either PAN or Aadhaar is required");
    }

    if (request.panNumber() != null && request.adhaarNumber()!= null) {
       throw new RuntimeException("Only one of PAN or Aadhaar allowed");
    }

    Kyc identity  = kycMapper.getEntity(request, keycloakId);
    kycRepo.save(identity);

    return "Data saved! wait for the verification";
  }

  public void  saveUrls(UUID keycloakId, String imgUrl, DocumentType type){
    if(!userRepo.existsByKeycloakId(keycloakId)){
      throw new UserExistsException("Please register urself first");
    }

    Kyc identity = kycRepo.findByKeycloakId(keycloakId);

    if(type == DocumentType.DOCUMENT){
      identity.setDocumentUrl(imgUrl);
    }else{
      identity.setSelfieUrl(imgUrl);
    }
    kycRepo.save(identity);
  }

  public void updateStatus(UUID id){
     UserInfo user = userRepo.findByKeycloakId(id).			orElseThrow(() -> new UserExistsException("please register urself first"));
     
     user.setKycStatus(KYCSTATUS.VERIFIED);
     userRepo.save(user);

  }
}
