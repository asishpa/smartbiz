package com.smartbiz.service;

import com.smartbiz.dto.DeliveryChargesDTO;
import com.smartbiz.model.AddDeliveryCharges;

public interface DeliveryService {
	public DeliveryChargesDTO updateDeliveryCharges(String storeId, AddDeliveryCharges deliveryCharges);
	public DeliveryChargesDTO getDeliveryCharges(String storeId);
}
