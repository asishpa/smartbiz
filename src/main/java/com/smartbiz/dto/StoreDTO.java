package com.smartbiz.dto;

import java.util.Date;

import lombok.Data;

@Data
public class StoreDTO {
	private Long id;
	private String name;
	private String ownerName;
	private Date createdAt;
	
}
