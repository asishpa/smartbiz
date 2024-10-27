package com.smartbiz.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartbiz.annotation.PublicEndpoint;
import com.smartbiz.dto.OfferDTO;
import com.smartbiz.dto.ProductsDTO;
import com.smartbiz.model.AddOffer;
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
	@PutMapping("/{storeId}/offers")
	public ResponseEntity<String> createOffer(@PathVariable String storeId,@RequestBody AddOffer offer){
		String offerId = offerService.createOffer(storeId, offer);
		return new ResponseEntity<>(offerId,HttpStatus.CREATED);
	}
	@DeleteMapping("/{storeId}/offers/{offerId}")
	public ResponseEntity<Map<String, String>> deleteOffer(@PathVariable String storeId,@PathVariable String offerId){
		boolean status = offerService.deleteOffer(storeId, offerId);
		Map<String, String> response = new HashMap<>();
		if (status) {
			response.put("msg", "Offer Deleted Successfully");
			return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
		}
		response.put("msg", "Something went wrong");
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	@GetMapping("/{storeId}/offers")
	public ResponseEntity<List<OfferDTO>> getOffers(@PathVariable String storeId){
		List<OfferDTO> offers = offerService.getOffers(storeId);
		return new ResponseEntity<>(offers,HttpStatus.OK);
	}
	@GetMapping("/public/{storeId}/offers")
	@PublicEndpoint
	public ResponseEntity<List<OfferDTO>> getVisibleOffer(@PathVariable String storeId){
		List<OfferDTO> offers = offerService.getVisibleOffers(storeId);
		return new ResponseEntity<>(offers,HttpStatus.OK);
		
	}
	
	

}
