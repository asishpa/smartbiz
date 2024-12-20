package com.smartbiz.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class AddProduct {
	private String productName;
	private String categoryId;
	private Float actualPrice;
	private Float discountedPrice;
	private List<String> photoPublicId;
	private Map<String, Integer> inventoryList;
	private Float weight;
	private String hsnCode;
	private String productDesc;
	private boolean active;

}
