package com.smartbiz.exceptions;

public class InvalidOfferException extends RuntimeException{
	public InvalidOfferException() {
		
	}
	public InvalidOfferException(String message) {
		super(message);
	}
}
