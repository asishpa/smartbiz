package com.smartbiz.service;

import com.smartbiz.dto.StoreDTO;
import com.smartbiz.model.StoreMetadata;

public interface StoreService {
	public StoreDTO getStoreMetadata(String storeId);
	public StoreDTO updateStoreMetadata(String storeId,StoreMetadata metadata);

}
