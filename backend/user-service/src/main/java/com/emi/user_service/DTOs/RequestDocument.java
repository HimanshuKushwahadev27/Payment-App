package com.emi.user_service.DTOs;

import com.emi.user_service.enums.DocumentType;

public record RequestDocument(

  String imgUrl,
  DocumentType type
) {
  
  
}
