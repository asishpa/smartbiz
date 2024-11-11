package com.smartbiz.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class OrderItemDTO {
	private Long id;
    private String productId;
    private Integer qty;
    private BigDecimal price;
    private Long version;
    private String productName;  // Added product name
    private String productDescription;  // Added product description
    private List<String> productImage;  
}
