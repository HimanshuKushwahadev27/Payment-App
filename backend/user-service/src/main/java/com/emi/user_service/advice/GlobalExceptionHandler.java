package com.emi.user_service.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.emi.user_service.exceptions.UserExistsException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserExistsException.class)
	public ResponseEntity<?> handleBookExistsFound(UserExistsException ex){
		return ResponseEntity
				.status(404)
				.body(ex.getMessage());
	}
}
