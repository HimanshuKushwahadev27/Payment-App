package com.emi.user_service.serviceImpl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.emi.user_service.DTOs.RequestDocument;
import com.emi.user_service.entity.Document;
import com.emi.user_service.enums.DocumentType;
import com.emi.user_service.exceptions.UserExistsException;
import com.emi.user_service.repository.DocumentRepo;
import com.emi.user_service.repository.UserRepo;
import com.emi.user_service.service.UserService;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
  
  @Value("${minio.endpoint}")
  private  String endpoint;

  @Value("${minio.public.endpoint}")
  private  String publicEndpoint;
  
  private final DocumentRepo documentRepo;
  private final UserRepo userRepo;

  @Qualifier("publicMinioEndpoint")
  private final MinioClient minioClient;

  private final String bucket = "user-documents";
  private final UserService userService;
  private final KycService kycService;

   public String uploadFile(MultipartFile file) throws Exception {
      String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

      minioClient.putObject(
         PutObjectArgs.builder()
               .bucket(bucket)
               .object(fileName)
               .stream(file.getInputStream(), file.getSize(), -1)
               .contentType(file.getContentType())
               .build()
      );

      return publicEndpoint + "/" + bucket + "/" + fileName;
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
          kycService.saveUrls(keycloakId, request.imgUrl(), DocumentType.DOCUMENT);
       }else if (request.type() == DocumentType.SELFIE_URL){
          kycService.saveUrls(keycloakId, request.imgUrl(), DocumentType.SELFIE_URL);
       }else if (request.type() == DocumentType.PROFILE_IMAGE){
         log.info("from fileService" + request.imgUrl());
          userService.saveImageUrl(request.imgUrl(), keycloakId);
       }
  }
}
