package com.smartbiz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartbiz.constants.AppConstants;
import com.smartbiz.dto.OrderDTO;
import com.smartbiz.entity.Orders;
import com.smartbiz.exceptions.ResourceNotFoundException;
import com.smartbiz.mapper.EntityMapper;
import com.smartbiz.repository.OrderRepository;

@Service
public class SellerOrderServiceImpl implements SellerOrderService{

	@Autowired
	private OrderRepository orderRepo;
	@Autowired
	private EntityMapper entityMapper;
	@Override
	public List<OrderDTO> getAllOrders(String storeId) {
		List<Orders> orders = orderRepo.findByStoreId(storeId);
		if (orders.isEmpty()) {
			throw new ResourceNotFoundException(AppConstants.ERROR_ORDER_NOT_FOUND_FOR_STORE);
		}
		return orders.stream().map(entityMapper::toOrderDTO).toList();
	}
	public OrderDTO getOrderById(String storeId,String orderId) {
		Orders order = orderRepo.findById(orderId).orElseThrow(() -> {
			throw new ResourceNotFoundException(AppConstants.ERROR_ORDER_NOT_FOUND);
		});
		if (!order.getStore().getId().equals(storeId)) {
	        throw new ResourceNotFoundException("Order does not belong to the specified store.");
	    }
	    
		
		return entityMapper.toOrderDTO(order);
	}
	

}
