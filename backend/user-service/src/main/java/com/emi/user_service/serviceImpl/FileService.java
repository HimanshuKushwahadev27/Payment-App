package com.emi.user_service.serviceImpl;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.emi.user_service.DTOs.RequestDocument;
import com.emi.user_service.entity.Document;
import com.emi.user_service.enums.DocumentType;
import com.emi.user_service.exceptions.UserExistsException;
import com.emi.user_service.repository.DocumentRepo;
import com.emi.user_service.repository.UserRepo;
import com.emi.user_service.service.UserService;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {
  
  @Value("${minio.endpoint}")
  private  String endpoint;

  @Value("${minio.public.endpoint}")
  private  String publicEndpoint;
  
  private final DocumentRepo documentRepo;
  private final UserRepo userRepo;
  private final MinioClient minioClient;
  private final String bucket = "user-documents";
  private final UserService userService;
  private final KycService keycService;

  public String getPresignedUrl(String fileName) throws Exception{
          String internalUrl = minioClient.getPresignedObjectUrl(
        GetPresignedObjectUrlArgs.builder()
            .method(Method.PUT)
            .bucket(bucket)
            .object(fileName)
            .expiry(10, TimeUnit.MINUTES)
            .build()
    );
    return internalUrl.replace(endpoint, publicEndpoint);
  }

  public void storeDocument(RequestDocument request, UUID keycloakId){
    if(!userRepo.existsByKeycloakId(keycloakId)){
      throw new UserExistsException("you r not registered as an user");      
    }
          Document document = new Document();
          document.setImgUrl(request.imgUrl());
          document.setKeycloakId(keycloakId);
          document.setType(request.type());

          documentRepo.save(document);
       if(request.type()==DocumentType.DOCUMENT){
          keycService.saveUrls(keycloakId, request.imgUrl(), DocumentType.DOCUMENT);
       }else if (request.type() == DocumentType.SELFIE_URL){
          keycService.saveUrls(keycloakId, request.imgUrl(), DocumentType.SELFIE_URL);
       }else{
          userService.saveImageUrl(document.getImgUrl(), keycloakId);
       }
  }
}
