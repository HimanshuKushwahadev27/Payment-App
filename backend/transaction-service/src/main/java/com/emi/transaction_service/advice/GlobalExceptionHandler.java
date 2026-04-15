package com.emi.transaction_service.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.emi.transaction_service.exception.TransactionNotFoundException;
import com.emi.transaction_service.exception.UnauthorizedException;

@RestControllerAdvice
public class GlobalExceptionHandler {


	@ExceptionHandler(TransactionNotFoundException.class)
	public ResponseEntity<?> handleBookExistsFound(TransactionNotFoundException ex){
		return ResponseEntity
				.status(404)
				.body(ex.getMessage());
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<?> handleBookExistsFound(UnauthorizedException ex){
		return ResponseEntity
				.status(401)
				.body(ex.getMessage());
	}

}
