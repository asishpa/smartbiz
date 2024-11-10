package com.smartbiz.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private BigDecimal orderAmt;
	private Integer items;
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;
	@Enumerated(EnumType.STRING)
	private FulfillmentType orderType;
	/*
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "address_id", nullable = false) private BuyerAddress
	 * buyerAddress;
	 */
	@OneToOne
	@JoinColumn(name = "offer_id", nullable = false)
	private Offer offer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private User customer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@CreationTimestamp
	private Date createdAt;

	public enum OrderStatus {
		PENDING, ACCEPTED, SHIPPED, PICKUP_READY, DELIVERED_OR_PICKED, CANCELLED, REJECTED
	}

	public enum PaymentMethod {
		COD, ONLINE
	}

	public enum FulfillmentType {
		DELIVERY, PICKUP
	}
}
