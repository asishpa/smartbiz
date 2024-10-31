package com.smartbiz.dto;

import java.util.Set;

import com.smartbiz.entity.Delivery.DELIVERY_CHARGE_TYPE;

import lombok.Data;

@Data
public class DeliveryChargesDTO {
	private Long id;
	private DELIVERY_CHARGE_TYPE chargeType;
    private Float chargePerOrder;
    private Float freeDeliveryAbove;
    private String deliveryHappensWithin;
    private Set<DeliveryRangeDTO> variableCharges;
}
