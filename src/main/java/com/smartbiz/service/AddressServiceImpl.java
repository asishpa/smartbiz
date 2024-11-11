package com.smartbiz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartbiz.constants.AppConstants;
import com.smartbiz.dto.BuyerAddressDTO;
import com.smartbiz.entity.BuyerAddress;
import com.smartbiz.entity.User;
import com.smartbiz.mapper.EntityMapper;
import com.smartbiz.model.AddBuyerAddress;
import com.smartbiz.repository.BuyerAddressRepository;
import com.smartbiz.repository.UserRepository;

@Service
public class AddressServiceImpl implements AddressService{

	@Autowired
	private BuyerAddressRepository buyerAddressRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private EntityMapper entityMapper;
	@Override
	public BuyerAddressDTO addBuyerAddress(String userId, AddBuyerAddress addAddress) {
		User user = userRepo.findById(userId).orElseThrow(() ->
			new RuntimeException(AppConstants.ERROR_USER_NOT_FOUND));
		BuyerAddress address = new BuyerAddress();
		address.setName(addAddress.getName());
		address.setMobileNo(addAddress.getMobileNo());
		address.setEmail(addAddress.getEmail());
		address.setAddress(addAddress.getAddress());
		address.setArea(addAddress.getArea());
		address.setLandmark(addAddress.getLandmark());
		address.setPinCode(addAddress.getPinCode());
		address.setCity(addAddress.getCity());
		address.setState(addAddress.getState());
		address.setCustomer(user);
		BuyerAddress buyerAddress = buyerAddressRepo.save(address);
		return entityMapper.toAddressDto(buyerAddress);
	}
	@Override
	public List<BuyerAddressDTO> getAddress(String userId) {
		
		List<BuyerAddress> addresses = buyerAddressRepo.findByCustomerUserId(userId);
		return entityMapper.toAddressDTO(addresses);
	}
	@Override
	public boolean deleteAddress(String userId, Long addressId) {
		if (buyerAddressRepo.existsById(addressId)) {
            buyerAddressRepo.deleteById(addressId);
            return true;  
        }
        return false;
	}
	

}
