package com.smartbiz.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class CartItemDTO {
	private String productId;
	private String productName;
	private Integer quantity;
	private BigDecimal discountedPrice;
	private BigDecimal actualPrice;
	private BigDecimal totalPrice;
	private List<String> productPhoto;
}
