package com.smartbiz.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartbiz.constants.AppConstants;
import com.smartbiz.dto.DeliveryChargesDTO;
import com.smartbiz.dto.DeliveryRangeDTO;
import com.smartbiz.entity.Delivery;
import com.smartbiz.entity.DeliveryRange;
import com.smartbiz.entity.Store;
import com.smartbiz.exceptions.ResourceNotFoundException;
import com.smartbiz.mapper.EntityMapper;
import com.smartbiz.model.AddDeliveryCharges;
import com.smartbiz.repository.DeliveryRepository;
import com.smartbiz.repository.StoreRepository;

@Service
public class DeliveryServiceImpl implements DeliveryService{

	@Autowired
	private StoreRepository storeRepo;
	@Autowired
	private DeliveryRepository deliveryRepo;
	
	@Autowired
	private EntityMapper entityMapper;
	@Override
	public DeliveryChargesDTO updateDeliveryCharges(String storeId, AddDeliveryCharges deliveryCharges) {
		Store store = storeRepo.findById(storeId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_STORE_NOT_FOUND));
		Delivery delivery = deliveryRepo.findByStore(store)
			.orElse(new Delivery());
		if (deliveryCharges.getDeliveryChargeType().equals(Delivery.DELIVERY_CHARGE_TYPE.FIXED)) {
			
			delivery.setChargePerOrder(deliveryCharges.getChargePerOrder());
			delivery.setFreeDeliveryAbove(deliveryCharges.getFreeDeliveryAbove());
			delivery.setVariableCharges(new HashSet<>());
		}
		else {
			Set<DeliveryRange> deliveryRanges = convertToDeliveryRanges(deliveryCharges.getVariableCharges(), delivery);
			delivery.setVariableCharges(deliveryRanges);
		}
			delivery.setStore(store);
			delivery.setChargeType(deliveryCharges.getDeliveryChargeType());
			delivery.setDeliveryHappensWithin(deliveryCharges.getDeliveryHappensWithin());
			Delivery savedDelivery = deliveryRepo.save(delivery);
			return entityMapper.toDeliveryChargeDTO(savedDelivery);
	}
	private Set<DeliveryRange> convertToDeliveryRanges(Set<DeliveryRangeDTO> rangeDTOs,Delivery delivery){
		validateRanges(rangeDTOs);
		return rangeDTOs.stream()
				.map(dto -> createDeliveryRange(dto, delivery))
				.collect(Collectors.toSet());
	}
	private DeliveryRange createDeliveryRange(DeliveryRangeDTO dto,Delivery delivery) {
		DeliveryRange range = new DeliveryRange();
		range.setStartAmt(dto.getStartAmt());
		range.setEndAmt(dto.getEndAmt());
		range.setCharge(dto.getCharge());
		range.setDelivery(delivery);
		return range;
	}
	private void validateRanges(Set<DeliveryRangeDTO> ranges) {
		//first individual validation to check start amount is less than the end amount
		ranges.forEach(range -> {
			if (range.getStartAmt() >= range.getEndAmt()) {
				throw new IllegalArgumentException("Start amt cannot be less than the end amt");
			}
			if (range.getCharge() <= 0) {
				throw new IllegalArgumentException("Charges cannot be negative");
				
			}
		});
		ranges.stream()
			.sorted((r1,r2) -> r1.getStartAmt().compareTo(r2.getStartAmt()))
			.reduce((r1,r2) -> {
				if (r1.getEndAmt() >= r2.getStartAmt()) {
					throw new IllegalArgumentException("Delivery charge range cannot overlap");
				}
				return r2;
			});
		
		
	}
	@Override
	public DeliveryChargesDTO getDeliveryCharges(String storeId) {
		Store store = storeRepo.findById(storeId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_STORE_NOT_FOUND));
		Delivery deliveryCharge = deliveryRepo.findByStore(store).orElseThrow(() -> new ResourceNotFoundException("Delivery Charges not found"));
		return entityMapper.toDeliveryChargeDTO(deliveryCharge);
	}
	
}
