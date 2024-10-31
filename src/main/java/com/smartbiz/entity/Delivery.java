package com.smartbiz.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private DELIVERY_CHARGE_TYPE chargeType;
	private Float chargePerOrder;
	private Float freeDeliveryAbove;
	private String deliveryHappensWithin;
	@OneToMany(mappedBy = "delivery",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private Set<DeliveryRange> variableCharges;
	public enum DELIVERY_CHARGE_TYPE{
		FIXED,
		VARIABLE
	}
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id",nullable = false)
	private Store store;
	
}
