package com.smartbiz.dto;

import java.math.BigDecimal;
import java.util.List;

import com.smartbiz.entity.CartItem;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartResponseDTO {
	private String cartId;
	private String storeId;
	private String storeName;
	private List<CartItemDTO> items;
	private BigDecimal deliveryCharge;
	private BigDecimal subTotal;
	private BigDecimal discount;
	private BigDecimal total;
	private OfferDTO appliedOfferId;
	private BuyerAddressDTO deliveryAddress;
}
