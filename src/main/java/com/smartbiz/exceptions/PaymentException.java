package com.smartbiz.exceptions;

public class PaymentException extends RuntimeException{
	public PaymentException() {
		
	}
	public PaymentException(String message) {
		super(message);
	}
}
