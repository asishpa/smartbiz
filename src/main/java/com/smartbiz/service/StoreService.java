package com.smartbiz.service;

import com.smartbiz.dto.DeliveryChargesDTO;
import com.smartbiz.dto.StoreDTO;
import com.smartbiz.model.AddDeliveryCharges;
import com.smartbiz.model.StoreMetadata;

public interface StoreService {
	public StoreDTO getStoreMetadata(String storeId);
	public StoreDTO updateStoreMetadata(String storeId,StoreMetadata metadata);
	public StoreDTO getStoreMetadataByStoreUrl(String storeUrl);
}
