package com.smartbiz.service;

import java.util.List;

import com.smartbiz.dto.OrderDTO;

public interface SellerOrderService {
	public List<OrderDTO> getAllOrders(String storeId);
	public OrderDTO getOrderById(String storeId,String orderId);
	
}
