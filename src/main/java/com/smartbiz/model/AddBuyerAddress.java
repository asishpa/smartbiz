package com.smartbiz.model;

import lombok.Data;

@Data
public class AddBuyerAddress {
	private String name;
	private Long mobileNo;
	private String email;
	private String address;
	private String area;
	private String landmark;
	private Long pinCode;
	private String city;
	private String state;
}
