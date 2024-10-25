package com.smartbiz.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartbiz.model.OfferValidationRequest;
import com.smartbiz.service.OfferService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/stores")
public class OfferController {
	
	@Autowired
	private OfferService offerService;
	@PostMapping("/{storeId}/validate-offer")
	public ResponseEntity<Map<String, String>> validateOffer(@PathVariable String storeId,@Valid @RequestBody OfferValidationRequest request){
		System.out.println("Received request: " + request);
		offerService.validateOffer(storeId, request);
		Map<String, String> response = new HashMap<>();
		response.put("msg", "Offer is valid");
		return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
	}
	

}
