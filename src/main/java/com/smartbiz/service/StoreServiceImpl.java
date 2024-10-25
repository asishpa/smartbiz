package com.smartbiz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartbiz.constants.AppConstants;
import com.smartbiz.dto.StoreDTO;
import com.smartbiz.entity.Store;
import com.smartbiz.exceptions.ResourceNotFoundException;
import com.smartbiz.mapper.EntityMapper;
import com.smartbiz.repository.StoreRepository;

import jakarta.persistence.EntityManager;

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
	
}
