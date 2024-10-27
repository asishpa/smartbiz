package com.smartbiz.service;

import java.util.List;

import com.smartbiz.dto.OfferDTO;
import com.smartbiz.model.AddOffer;
import com.smartbiz.model.OfferValidationRequest;

public interface OfferService {

	void validateOffer(String storeId, OfferValidationRequest request);
	String createOffer(String storeId,AddOffer offer);
	List<OfferDTO> getOffers(String storeId);
	boolean deleteOffer(String storeId,String offerId);
	List<OfferDTO> getVisibleOffers(String storeId);
}
	