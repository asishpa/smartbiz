package com.smartbiz.exceptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.cloudinary.Api;

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

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		ApiError errorDetails = new ApiError(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(OfferExistsException.class)
	public ResponseEntity<ApiError> handleOfferExistsException(OfferExistsException ex, WebRequest request) {
		ApiError errorDetails = new ApiError(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex){
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMsg = error.getDefaultMessage();
			errors.put(fieldName, errorMsg);
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex,WebRequest request){
		ApiError errorDetails = new ApiError(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails,HttpStatus.FORBIDDEN);
	}
	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ApiError> handleProductNotFoundException(ProductNotFoundException ex,WebRequest request){
		ApiError errorDetails = new ApiError(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(InsufficientInventoryException.class)
	public ResponseEntity<ApiError> handleInsufficientInventoryException(InsufficientInventoryException ex,WebRequest request){
		ApiError errorDetails = new ApiError(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails,HttpStatus.PRECONDITION_FAILED);
	}
	@ExceptionHandler(PaymentException.class)
	public ResponseEntity<ApiError> handlePaymentException(PaymentException ex,WebRequest request){
		ApiError errorDetails = new ApiError(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails,HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
