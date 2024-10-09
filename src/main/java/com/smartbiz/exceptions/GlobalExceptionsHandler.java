package com.smartbiz.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import jakarta.annotation.PostConstruct;

@RestControllerAdvice
public class GlobalExceptionsHandler {

	@PostConstruct
	public void init() {
		System.out.println("GlobalExceptionsHandler initialized");
	}

	@ExceptionHandler(UserExistsException.class)
	public ResponseEntity<ApiError> handleUserExistsException(UserExistsException ex, WebRequest request) {
		ApiError errorDetails = new ApiError(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(RoleNotFoundException.class)
	public ResponseEntity<ApiError> handleRoleNotFoundException(RoleNotFoundException ex, WebRequest request) {
		ApiError errorDetails = new ApiError(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ApiError> handlerInvalidCredentialException(InvalidCredentialsException ex,
			WebRequest request) {
		System.out.println("entered-test");
		ApiError errorDetails = new ApiError(new Date(), ex.getMessage(), request.getDescription(false));
		System.out.println(errorDetails);
		return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(UnauthorizedAccessException.class)
	public ResponseEntity<ApiError> handleUnAuthorizedAccessException(UnauthorizedAccessException ex,
			WebRequest request) {
		ApiError errorDetails = new ApiError(new Date(), ex.getMessage(), request.getDescription(false));

		return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
	}
}
