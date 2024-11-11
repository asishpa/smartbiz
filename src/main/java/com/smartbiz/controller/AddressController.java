package com.smartbiz.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartbiz.dto.BuyerAddressDTO;
import com.smartbiz.entity.BuyerAddress;
import com.smartbiz.model.AddBuyerAddress;
import com.smartbiz.service.AddressService;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class AddressController {
	
	@Autowired
	private AddressService addressService;
	
	@PutMapping("/{userId}/address")
	public ResponseEntity<BuyerAddressDTO> addAddress(@PathVariable String userId,@RequestBody AddBuyerAddress buyerAddress){
		BuyerAddressDTO address = addressService.addBuyerAddress(userId, buyerAddress);
		return new ResponseEntity<>(address,HttpStatus.CREATED);
	}
	@GetMapping("/{userId}/address")
	public ResponseEntity<List<BuyerAddressDTO>> getAddress(@PathVariable String userId){
		List<BuyerAddressDTO> address = addressService.getAddress(userId);
		return new ResponseEntity<>(address,HttpStatus.OK);
	}
	@DeleteMapping("/{userId}/address/{addressId}")
	public ResponseEntity<Map<String, String>> deleteAddress(@PathVariable String userId,@PathVariable Long addressId){
		Map<String, String> response = new HashMap<>();
		boolean isDeleted = addressService.deleteAddress(userId, addressId);
		if (isDeleted) {
			response.put("msg", "Address deleted");
			return new ResponseEntity<>(response,HttpStatus.OK);
		}
		else {
			response.put("msg", "Address not found");
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		
	}
}
