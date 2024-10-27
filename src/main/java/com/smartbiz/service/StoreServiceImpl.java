package com.smartbiz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartbiz.constants.AppConstants;
import com.smartbiz.dto.StoreDTO;
import com.smartbiz.entity.Store;
import com.smartbiz.exceptions.ResourceNotFoundException;
import com.smartbiz.mapper.EntityMapper;
import com.smartbiz.model.StoreMetadata;
import com.smartbiz.repository.StoreRepository;

@Service
public class StoreServiceImpl implements StoreService {

	@Autowired
	private StoreRepository storeRepo;
	
	@Autowired
	private EntityMapper entityMapper;
	@Override
	public StoreDTO getStoreMetadata(String storeId) {
		Store store = storeRepo.findById(storeId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_STORE_NOT_FOUND));
		return entityMapper.toStoreDTO(store);
	}
	public StoreDTO updateStoreMetadata(String storeId,StoreMetadata newMetadata) {
		Store existingMetadata = storeRepo.findById(storeId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_STORE_NOT_FOUND));
		existingMetadata.setName(newMetadata.getName());
		existingMetadata.setStoreLink(newMetadata.getStoreLink());
		existingMetadata.setMobileNo(newMetadata.getMobileNo());
		existingMetadata.setCountry(newMetadata.getCountry());
		existingMetadata.setStoreAddress(newMetadata.getStoreAddress());
		storeRepo.save(existingMetadata);
		return entityMapper.toStoreDTO(existingMetadata);
	}
	@Override
	public StoreDTO getStoreMetadataByStoreName(String storeName) {
		Store store = storeRepo.findByName(storeName).orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_STORE_NOT_FOUND));
		return entityMapper.toStoreDTO(store);
	}
	
}
