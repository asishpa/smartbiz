package com.smartbiz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartbiz.entity.Offer;
import com.smartbiz.entity.Store;
import com.smartbiz.exceptions.OfferExistsException;
import com.smartbiz.exceptions.ResourceNotFoundException;
import com.smartbiz.model.AddOffer;
import com.smartbiz.model.OfferValidationRequest;
import com.smartbiz.repository.OfferRepository;
import com.smartbiz.repository.StoreRepository;

import net.bytebuddy.implementation.bytecode.Addition;

@Service
public class OfferServiceImpl implements OfferService {

	@Autowired
	private OfferRepository offerRepo;

	@Autowired
	private StoreRepository storeRepo;

	@Override
	public void validateOffer(String storeId, OfferValidationRequest request) {
		if (offerRepo.existsByofferNameAndStoreId(storeId, request.getOfferName())) {
			throw new OfferExistsException("Offer exists with name:" + request.getOfferName());

		}
		if (offerRepo.existsByofferCodeAndStoreId(storeId, request.getOfferCode())) {
			throw new OfferExistsException("Offer exists with code:" + request.getOfferCode());
		}

	}

	@Override
	public String createOffer(String storeId, AddOffer addOffer) {
		Store store = storeRepo.findById(storeId)
				.orElseThrow(() -> new ResourceNotFoundException("Store does not exist with given ID"));
		Offer offer = new Offer();
		if (offer.getOfferType().equals("PERCENTAGE_DISCOUNT")) {
			offer.setOfferName(addOffer.getOfferName());
			offer.setOfferCode(addOffer.getOfferCode());
			offer.setOfferType(addOffer.getOfferType());
			offer.setVisibilityType(addOffer.getVisibilityType());
			offer.setPercentageValue(addOffer.getPercentageValue());
			offer.setMinimumPurchaseAmount(addOffer.getMinimumPurchaseAmount());
			offer.setMaximumDiscountAmount(addOffer.getMaximumDiscountAmount());
			offer.setUsageType(addOffer.getUsageType());
			offer.setStartDate(addOffer.getStartDate());
			offer.setEndDate(addOffer.getEndDate());
			offer.setCustomerType(addOffer.getCustomerType());
			offer.setStore(store);

		} else {
			offer.setOfferName(addOffer.getOfferName());
			offer.setVisibilityType(addOffer.getVisibilityType());
			offer.setOfferCode(addOffer.getOfferCode());
			offer.setFlatAmountValue(addOffer.getFlatAmountValue());
			offer.setMinimumPurchaseAmount(addOffer.getMinimumPurchaseAmount());
			offer.setUsageType(addOffer.getUsageType());
			offer.setStartDate(addOffer.getStartDate());
			offer.setEndDate(addOffer.getEndDate());
			offer.setCustomerType(addOffer.getCustomerType());
			offer.setStore(store);

		}
		Offer newOffer = offerRepo.save(offer);
		return newOffer.getId();
	}

}
