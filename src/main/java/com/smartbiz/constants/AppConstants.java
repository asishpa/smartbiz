package com.smartbiz.constants;

public class AppConstants {

	private AppConstants() {
		// private constructor to prevent instantiation
	}

	public static final String INVALID_CRED = "Invalid username/password";
	public static final String SUCCESS = "SUCCESS";
	public static final String ERROR_USER_EXISTS = "User with this email already exists";
	public static final String ERROR_UNAUTHORIZED_ACCESS = "User is not registered as a seller";
	public static final String ERROR_ROLE_NOT_FOUND = "Role not found";
	public static final String ERROR_STORE_NOT_FOUND = "Store not found";
}
