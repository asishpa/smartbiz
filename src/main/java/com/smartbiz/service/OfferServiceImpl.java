package com.smartbiz.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartbiz.constants.AppConstants;
import com.smartbiz.dto.OfferDTO;
import com.smartbiz.entity.Offer;
import com.smartbiz.entity.Store;
import com.smartbiz.exceptions.OfferExistsException;
import com.smartbiz.exceptions.ResourceNotFoundException;
import com.smartbiz.mapper.EntityMapper;
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
	
	@Autowired
	private EntityMapper entityMapper;

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
		 offer.setOfferName(addOffer.getOfferName());
		    offer.setOfferCode(addOffer.getOfferCode());
		    offer.setVisibilityType(addOffer.getVisibilityType());
		    offer.setMinimumPurchaseAmount(addOffer.getMinimumPurchaseAmount());
		    offer.setStartDate(addOffer.getStartDate());
		    offer.setEndDate(addOffer.getEndDate());
		    offer.setCustomerType(addOffer.getCustomerType());
		    offer.setStore(store);

		    // Set offer type specific fields
		    if (addOffer.getOfferType().equals(Offer.OfferType.PERCENTAGE_DISCOUNT)) {
		        offer.setOfferType(addOffer.getOfferType());
		        offer.setPercentageValue(addOffer.getPercentageValue());
		    } else {
		        offer.setOfferType(addOffer.getOfferType());
		        offer.setFlatAmountValue(addOffer.getFlatAmountValue());
		    }

		    // Set usage limit if usage type is CUSTOM
		    if (addOffer.getUsageType().equals(Offer.UsageType.CUSTOM)) {
		        offer.setUsageLimit(addOffer.getUsageLimit()); // Ensure this is handled in your AddOffer model
		    }

		    Offer newOffer = offerRepo.save(offer);
		    return newOffer.getId();
	}

	@Override
	public boolean deleteOffer(String storeId, String offerId) {
		Offer offer = offerRepo.findById(offerId).orElseThrow(() -> new ResourceNotFoundException("Offer not found with given Id Id"));
		try {
			Store store = offer.getStore();
			if (store!=null && store.getOffers() != null) {
					store.getOffers().remove(offer);
			}
			offerRepo.delete(offer);
			return true;
		} catch (Exception e) {
			throw new RuntimeException("Failed to delete offer");
		}
	}

	@Override
	public List<OfferDTO> getOffers(String storeId) {
		Store store =storeRepo.findById(storeId).orElseThrow(() -> new RuntimeException(AppConstants.ERROR_STORE_NOT_FOUND));
		List<Offer> offers = offerRepo.findByStore(store);
		return entityMapper.toOfferDTO(offers);
	}

	@Override
	public List<OfferDTO> getVisibleOffers(String storeId) {
		Store store = storeRepo.findById(storeId)
	            .orElseThrow(() -> new RuntimeException(AppConstants.ERROR_STORE_NOT_FOUND));
	    List<Offer> offers = offerRepo.findByStore(store);
	    
	    // Correctly mapping List<Offer> to List<OfferDTO>
	    return offers.stream()
                .filter(offer -> offer.getVisibilityType() == Offer.VisibilityType.VISIBLE_ON_STORE)
                .map(entityMapper::toOfferDTO)
                .collect(Collectors.toList());
	}
	

}
