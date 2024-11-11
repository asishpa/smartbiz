package com.smartbiz.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private BigDecimal orderAmt;
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;
	@Enumerated(EnumType.STRING)
	private FulfillmentType orderType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id", nullable = false)
	private BuyerAddress buyerAddress;

	@OneToMany(mappedBy = "order")
	// Orders items/statusHistory by createdAt in descending order, so the most
	// recent entries appear first.
	@OrderBy("createdAt DESC")
	private List<OrderStatusHistory> statusHistory = new ArrayList<>();

	@OneToOne
	@JoinColumn(name = "offer_id", nullable = false)
	private Offer offer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private User customer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> items = new ArrayList<>();
	@CreationTimestamp
	private Date createdAt;

	// helper methods
	public void addItem(OrderItem item) {
		
		if (this.items == null) {
	        //System.out.println("Items list is null; initializing a new list");
	        System.out.println("null in items list");
			this.items = new ArrayList<>();
	    }
		item.setOrder(this);  
	    items.add(item);
	    System.out.println("exited out of addItem");
	}

	public void removeItem(OrderItem item) {
		item.setOrder(null);
		items.remove(item);
	}

	public void updateStatus(OrderStatus newStatus, String comment) {
		OrderStatusHistory history = new OrderStatusHistory();
		history.setOrder(this);
		history.setPreviousStatus(this.status);
		history.setNewStatus(newStatus);
		history.setComment(comment);

		this.status = newStatus;
		this.statusHistory.add(history);
	}

	public enum OrderStatus {
		PENDING("Pending"), ACCEPTED("Accepted"), SHIPPED("Shipped"), PICKUP_READY("Pickup Ready"),
		DELIVERED_OR_PICKED("Delivered/Picked"), CANCELLED("Cancelled"), REJECTED("Rejected");

		private final String displayName;

		OrderStatus(String displayName) {
			this.displayName = displayName;
		}

		public String getDisplayName() {
			return displayName;
		}
	}

	public enum PaymentMethod {
		COD, ONLINE
	}

	public enum FulfillmentType {
		DELIVERY, PICKUP
	}
}
