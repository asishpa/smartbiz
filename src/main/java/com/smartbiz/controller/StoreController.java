package com.smartbiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartbiz.dto.StoreDTO;
import com.smartbiz.model.StoreMetadata;
import com.smartbiz.service.StoreService;

@RestController
@RequestMapping("/api/stores")
public class StoreController {
	@Autowired
	private StoreService storeService;
	@GetMapping("/{storeId}/metadata")
	public ResponseEntity<StoreDTO> getStoreMetadata(@PathVariable String storeId){
		StoreDTO store = storeService.getStoreMetadata(storeId);
		return new ResponseEntity<>(store,HttpStatus.OK);
	}
	@PatchMapping("/{storeId}/metadata")
	public ResponseEntity<StoreDTO> updateStoreMetadata(@PathVariable String storeId,@RequestBody StoreMetadata metadata){
		StoreDTO store = storeService.updateStoreMetadata(storeId, metadata);
		return new ResponseEntity<>(store,HttpStatus.ACCEPTED);
	}

}
