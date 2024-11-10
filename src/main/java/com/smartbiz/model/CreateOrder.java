package com.smartbiz.model;

import com.smartbiz.entity.Orders.FulfillmentType;
import com.smartbiz.entity.Orders.PaymentMethod;

import lombok.Data;

@Data
public class CreateOrder {
	private String cartId;
	private FulfillmentType fulfillmentType;
	private PaymentMethod paymentMethod;

}
