package com.smartbiz.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddToCart {
	@NotNull(message = "store ID cannot be null")
	private String storeId;
	@NotNull(message = "product ID cannot be null")
	private String productId;
}
