package com.smartbiz.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductsDTO {
	private String id;
    private String productName;
    private String categoryName;
    private String productDesc;
    private Float actualPrice;
    private Float discountedPrice;
    private Float weight;
    private String hsnCode;
    private String storeName;
    private boolean isActive;
    private List<String> photoPublicId;
    private List<ProductWarehouseDTO> inventory;
}
