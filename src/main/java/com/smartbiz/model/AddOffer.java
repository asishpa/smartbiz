package com.smartbiz.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.smartbiz.entity.Offer.CustomerType;
import com.smartbiz.entity.Offer.OfferType;
import com.smartbiz.entity.Offer.UsageType;
import com.smartbiz.entity.Offer.VisibilityType;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddOffer {
	
	@NotBlank
	private String offerName;
	private String offerCode;
	private OfferType offerType;
	@DecimalMin(value = "0.0", inclusive = true, message = "Percentage value must be at least 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "Percentage value cannot exceed 100")
    @Digits(integer = 3, fraction = 2, message = "Percentage value must have at most 3 integer digits and 2 decimal places")
	private BigDecimal percentageValue;
	@DecimalMin(value = "0.0", inclusive = true, message = "Discount amount must be at least 0")
	private BigDecimal flatAmountValue;
	@NotNull(message = "Minimum purchase amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Minimum purchase amount must be at least 0")
    private BigDecimal minimumPurchaseAmount;
	@DecimalMin(value = "0.0", inclusive = true, message = "Maximum discount amount must be at least 0")
    private BigDecimal maximumDiscountAmount;
	@NotNull(message = "start Date cannot be null")
	@FutureOrPresent(message = "start Date cannot be in the past")
	private LocalDate startDate;
	private LocalDate endDate;

    @NotNull(message = "Visibility type is required")
    private VisibilityType visibilityType;

    @NotNull(message = "Usage type is required")
    private UsageType usageType;

    @NotNull(message = "Customer type is required")
    private CustomerType customerType;

    private boolean isActive = true;
	
}
