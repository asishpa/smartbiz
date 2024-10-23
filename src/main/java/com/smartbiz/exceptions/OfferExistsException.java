package com.smartbiz.exceptions;

public class OfferExistsException extends RuntimeException{
	public OfferExistsException() {
		
	}
	public OfferExistsException(String message) {
		super(message);
		
	}
}
