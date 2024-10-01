package com.smartbiz.exceptions;

public class UserExistsException extends RuntimeException{
	public UserExistsException(String messsage) {
		super(messsage);
	}

}
