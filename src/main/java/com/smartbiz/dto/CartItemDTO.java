package com.smartbiz.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemDTO {
	private String productId;
	private String productName;
	private Integer quantity;
	private BigDecimal discountedPrice;
	private BigDecimal actualPrice;
	private List<String> productPhoto;
}
