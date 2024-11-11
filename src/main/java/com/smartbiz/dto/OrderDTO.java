package com.smartbiz.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.smartbiz.entity.BuyerAddress;
import com.smartbiz.entity.OrderItem;
import com.smartbiz.entity.Orders.FulfillmentType;
import com.smartbiz.entity.Orders.OrderStatus;
import com.smartbiz.entity.Orders.PaymentMethod;

import lombok.Data;

@Data
public class OrderDTO {
	private String id;
	private BigDecimal orderAmt;
	private List<OrderItemDTO> items;
	private OfferDTO offer;
	private BuyerAddressDTO buyerAddress;
	private OrderStatus status;
	private PaymentMethod paymentMethod;
	private FulfillmentType orderType;
	private String customerName; // Assuming the customer's name is needed
	private String storeName; // Assuming the store's name is needed
	private LocalDateTime createdAt;
}
