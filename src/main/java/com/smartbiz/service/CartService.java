package com.smartbiz.service;

import com.smartbiz.dto.CartResponseDTO;
import com.smartbiz.model.CartAction;

public interface CartService {
	public CartResponseDTO addItemToCart(String userId, CartAction addItemToCart);
	public CartResponseDTO getCart(String userId,String storeId);
	public CartResponseDTO applyOffer(String userId,String storeId,String offerId);
	public CartResponseDTO decreaseQuantityFromCart(String userId,CartAction removeItemFromCart,boolean removeCompletely);
	public CartResponseDTO removeOffer(String userId,String storeId);
}
