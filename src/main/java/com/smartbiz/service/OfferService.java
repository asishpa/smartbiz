package com.smartbiz.service;

import com.smartbiz.model.AddOffer;
import com.smartbiz.model.OfferValidationRequest;

public interface OfferService {

	void validateOffer(String storeId, OfferValidationRequest request);
	String createOffer(String storeId,AddOffer offer);
}
	