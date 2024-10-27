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
		Store store = storeRepo.findById(storeId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_STORE_NOT_FOUND));
		StoreDTO storeDTO = entityMapper.toStoreDTO(store);

		// Create a temporary response with owner's name included
		String ownerName = (store.getOwner() != null) ? store.getOwner().getUserName() : null;

		// Assuming StoreDTO has a method to include owner's name in the response
		storeDTO.setOwnerName(ownerName); // Call a setter method if it exists or return a response in a different
											// manner

		return storeDTO;
	}

	public StoreDTO updateStoreMetadata(String storeId, StoreMetadata newMetadata) {
		Store existingMetadata = storeRepo.findById(storeId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_STORE_NOT_FOUND));
		existingMetadata.setName(newMetadata.getName());
		existingMetadata.setStoreLink(newMetadata.getStoreLink());
		existingMetadata.setMobileNo(newMetadata.getMobileNo());
		existingMetadata.setCountry(newMetadata.getCountry());
		existingMetadata.setStoreAddress(newMetadata.getStoreAddress());
		existingMetadata.setStoreEmail(newMetadata.getStoreEmail());
		storeRepo.save(existingMetadata);
		StoreDTO storeDTO = entityMapper.toStoreDTO(existingMetadata);

		// Include owner's name in the response
		String ownerName = (existingMetadata.getOwner() != null) ? existingMetadata.getOwner().getUserName() : null;
		storeDTO.setOwnerName(ownerName); // Call a setter method if it exists

		return storeDTO;
	}

	@Override
	public StoreDTO getStoreMetadataByStoreUrl(String storeLink) {
		Store store = storeRepo.findByStoreLink(storeLink)
	            .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_STORE_NOT_FOUND));
	    StoreDTO storeDTO = entityMapper.toStoreDTO(store);

	    // Include owner's name in the response
	    String ownerName = (store.getOwner() != null) ? store.getOwner().getUserName() : null;
	    storeDTO.setOwnerName(ownerName); // Call a setter method if it exists

	    return storeDTO;
	}

	public String getOwnerNameByStoreId(String storeId) {
		Store store = storeRepo.findById(storeId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_STORE_NOT_FOUND));
		return store.getOwner().getUserName(); // Assuming getUserName() is defined in User entity
	}

}
