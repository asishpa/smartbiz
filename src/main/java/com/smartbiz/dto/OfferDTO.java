package com.smartbiz.dto;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Data;

@Data
public class OfferDTO {
	private String id;
	private String offerName;
	private String offerCode;
	private boolean active;
	@DecimalMin(value = "0.0",inclusive = true,message = "Percentage value must be at least 0")
	@DecimalMax(value = "100.0",inclusive = true,message = "Percentage value cannot exceed 100")
	@Digits(integer = 3,fraction = 2,message = "Percentage value must have at most 3 integer digits and 2 decimal places")
	private BigDecimal percentageValue;
	private BigDecimal flatAmountValue;
	private BigDecimal minimumPurchaseAmount;
	private BigDecimal maximumDiscountAmount;
	
}
