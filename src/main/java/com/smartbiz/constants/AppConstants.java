package com.smartbiz.constants;

public class AppConstants {

	private AppConstants() {
		// private constructor to prevent instantiation
	}

	public static final String INVALID_CRED = "Invalid username/password";
	public static final String SUCCESS = "SUCCESS";
	public static final String ERROR_USER_EXISTS = "User with this email already exists";
	public static final String ERROR_UNAUTHORIZED_ACCESS_SELLER= "User is not registered as a seller";
	public static final String ERROR_UNAUTHORIZED_ACCESS_BUYER= "User is not registered as a buyer";
	public static final String ERROR_ROLE_NOT_FOUND = "Role not found";
	public static final String ERROR_STORE_NOT_FOUND = "Store not found";
	public static final String SOMETHING_WENT_WRONG = "Something went wrong";
	public static final String ERROR_USER_NOT_FOUND = "User not found";
	public static final String ERROR_PRODUCT_NOT_FOUND = "Product not found";
	public static final String INSUFFICIENT_QUANTITY = "Requested quantity exceeds available stock across warehouses";
	public static final String ERROR_CATEGORY_NOT_FOUND = "Category not found";
	public static final String ERROR_OFFER_NOT_FOUND = "Offer not found";
	public static final String OFFER_NOT_APPLICABLE_AT_STORE = "Offer not applicable at this store";
	public static final String ERROR_CART_NOT_FOUND = "Cart not found";
	public static final String OFFER_NOT_APPLICABLE_AT_CART = "Offer not applicable at cart";
	public static final String ERROR_ADDRESS_NOT_FOUND = "Address not found";
	public static final String ERROR_CART_EMPTY = "Cart is empty";
	public static final String ERROR_ORDER_CREATE_FAIL = "Error while creating order";
	public static final String ERROR_ORDER_NOT_FOUND_FOR_STORE = "No orders found in this store";
	public static final String ERROR_ORDER_NOT_FOUND = "No orders with given id";
}
