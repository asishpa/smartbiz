package com.smartbiz.service;

import java.util.List;

import com.smartbiz.dto.OrderDTO;
import com.smartbiz.model.CreateOrder;

public interface BuyerOrderService {
	public OrderDTO createOrder(String userId,String storeId, CreateOrder createOrder);
	public List<OrderDTO> getOrders(String userId,String storeId);
}
