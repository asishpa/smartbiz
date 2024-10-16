package com.smartbiz.model;

import lombok.Data;

@Data
public class AddWarehouse {
	private String warehouseName;
	private String contactPerson;
	private Long mobileNo;
	private String flatHouseNo;
	private String areaColony;
	private Long pinCode;
	private String city;
	private String state;
}
