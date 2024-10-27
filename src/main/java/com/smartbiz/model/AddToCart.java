package com.smartbiz.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddToCart {
	@NotNull(message = "customer ID cannot be null")
	private String customerId;
	@NotNull(message = "product ID cannot be null")
	private String productId;
	@Min(value = 1,message = "Quantity must be atleast 1")
	private Integer quantity;
}
