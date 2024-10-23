package com.smartbiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseDTO {
	private String warehouseId;
	private String warehouseName;
	private String contactPerson;
	private Long mobileNo;
	private String flatHouseNo;
	private String areaColony;
	private Long pinCode;
	private String city;
	private String state; 
}
