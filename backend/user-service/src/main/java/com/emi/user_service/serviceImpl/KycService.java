package com.emi.user_service.serviceImpl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.emi.user_service.DTOs.KycRequest;
import com.emi.user_service.entity.Kyc;
import com.emi.user_service.enums.DocumentType;
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
}
