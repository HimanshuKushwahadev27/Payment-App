package com.emi.wallet_service.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.emi.wallet_service.exception.AccountNotExistsException;
import com.emi.wallet_service.exception.LedgerNotFoundException;
import com.emi.wallet_service.exception.UnauthorizedException;
import com.emi.wallet_service.exception.WalletNotExistsException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AccountNotExistsException.class)
	public ResponseEntity<?> handleContentNotFound(AccountNotExistsException  ex){
		return  ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(ex.getMessage());
	}


   @ExceptionHandler(LedgerNotFoundException.class)
	public ResponseEntity<?> handleContentNotFound(LedgerNotFoundException  ex){
		return  ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(ex.getMessage());
	}

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<?> handleContentNotFound(UnauthorizedException  ex){
    return  ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(ex.getMessage());
  }

  @ExceptionHandler(WalletNotExistsException.class)
  public ResponseEntity<?> handleContentNotFound(WalletNotExistsException  ex){
    return  ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ex.getMessage());
  }



  
}
