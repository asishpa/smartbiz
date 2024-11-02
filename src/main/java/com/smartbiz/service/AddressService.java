package com.smartbiz.service;

import java.util.List;

import com.smartbiz.dto.BuyerAddressDTO;
import com.smartbiz.entity.BuyerAddress;
import com.smartbiz.model.AddBuyerAddress;

public interface AddressService {
	public BuyerAddress addBuyerAddress(String userId, AddBuyerAddress address);
	public List<BuyerAddressDTO> getAddress(String userId);
	public boolean deleteAddress(String userId,Long addressId);
}
