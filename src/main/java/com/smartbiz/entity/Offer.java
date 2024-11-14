package com.smartbiz.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.aspectj.weaver.tools.Trace;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Offer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	@Column(nullable = false,length = 17,unique = true)
	private String offerName;
	@Column(nullable = false,length = 17,unique = true)
	private String offerCode;
	private boolean active;
	@DecimalMin(value = "0.0",inclusive = true,message = "Percentage value must be at least 0")
	@DecimalMax(value = "100.0",inclusive = true,message = "Percentage value cannot exceed 100")
	@Digits(integer = 3,fraction = 2,message = "Percentage value must have at most 3 integer digits and 2 decimal places")
	private BigDecimal percentageValue;
	private BigDecimal flatAmountValue;
	@Column(nullable = false)
	private BigDecimal minimumPurchaseAmount;
	@Column(nullable = false)
	private BigDecimal maximumDiscountAmount;
	@ManyToOne
	@JoinColumn(name = "store_id")
	private Store store;
	@Enumerated(EnumType.STRING)
	private OfferType offerType;
	
	@Enumerated(EnumType.STRING)
	private VisibilityType visibilityType;
	
	@Enumerated(EnumType.STRING)
	private UsageType usageType;
	
	@Enumerated(EnumType.STRING)
	private CustomerType customerType;
	@EqualsAndHashCode.Exclude
	@OneToOne(mappedBy = "appliedOffer", fetch = FetchType.LAZY)
    private Cart cart;
	@NotNull(message = "start Date cannot be null")
	@FutureOrPresent(message = "Start Date cannot be in the past")
	private LocalDate startDate;
	
	private LocalDate endDate;
	@Column(nullable = true)
	private Integer usageLimit;
	@CreationTimestamp
	private LocalDateTime createdAt;
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	public enum OfferType{
		PERCENTAGE_DISCOUNT,
		FLAT_AMOUNT_DISCOUNT
	}
	public enum VisibilityType{
		VISIBLE_ON_STORE,
		HIDDEN_ON_STORE
	}
	public enum UsageType{
		ONLY_ONCE,
		CUSTOM
	}
	public enum CustomerType{
		ANY_CUSTOMER,
		FIRST_CUSTOMER,
		REPEAT_CUSTOMER
	}
	
	
}
