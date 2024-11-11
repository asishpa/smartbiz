package com.smartbiz.service;

import java.util.List;

import com.smartbiz.dto.OrderDTO;
import com.smartbiz.model.CreateOrder;

public interface BuyerOrderService {
	 OrderDTO createOrder(String userId, String storeId, CreateOrder createOrder);
	 List<OrderDTO> getAllOrders(String userId, String storeId);
	 OrderDTO getOrderById(String userId, String orderId);
	 public OrderDTO cancelOrder(String StoreId,String orderId);
}

