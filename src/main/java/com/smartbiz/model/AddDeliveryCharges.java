package com.smartbiz.model;

import java.util.Set;

import com.smartbiz.dto.DeliveryRangeDTO;
import com.smartbiz.entity.Delivery.DELIVERY_CHARGE_TYPE;

import lombok.Data;

@Data
public class AddDeliveryCharges {
	private DELIVERY_CHARGE_TYPE deliveryChargeType;
	private Float chargePerOrder;
	private Float freeDeliveryAbove;
	private String deliveryHappensWithin;
	private Set<DeliveryRangeDTO> variableCharges;
}
