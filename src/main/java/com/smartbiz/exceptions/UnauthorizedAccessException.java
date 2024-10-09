package com.smartbiz.exceptions;

public class UnauthorizedAccessException extends RuntimeException{
	public UnauthorizedAccessException(String message) {
		super(message);
	}

}
