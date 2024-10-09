package com.smartbiz.exceptions;

public class InvalidCredentialsException extends RuntimeException{
	
	public InvalidCredentialsException() {
		
	}
	public InvalidCredentialsException(String message) {
		
		super(message);
		System.out.println(message);
	}
}
