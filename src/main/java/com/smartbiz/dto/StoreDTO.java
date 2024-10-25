package com.smartbiz.dto;

import java.util.Date;

import lombok.Data;

@Data
public class StoreDTO {
	private String id;
	private String name;
	private String ownerName;
	private String storeLink;
	private Long mobileNo;
	private String storeEmail;
	private String country;
	private String storeAddress;
	private Date createdAt;
}
