package com.smartbiz.exceptions;

public class InsufficientInventoryException extends RuntimeException{
	public InsufficientInventoryException() {
		
	}
	public InsufficientInventoryException(String message) {
		super(message);
	}
}
