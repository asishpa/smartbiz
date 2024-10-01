package com.smartbiz.exceptions;

import java.util.Date;

import lombok.Data;

@Data
public class ApiError {
	private Date timestamp;
	private String message;
	private String details;
	
	public ApiError(Date timestamp,String message,String details) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}
	
	

}
