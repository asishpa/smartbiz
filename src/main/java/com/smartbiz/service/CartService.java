package com.smartbiz.service;

import com.smartbiz.dto.CartResponseDTO;
import com.smartbiz.model.AddToCart;

public interface CartService {
	public CartResponseDTO addItemToCart(String userId, AddToCart addCart);
	public CartResponseDTO getCart(String userId,String storeId);
	public CartResponseDTO applyOffer(String userId,String storeId,String offerId);
}
