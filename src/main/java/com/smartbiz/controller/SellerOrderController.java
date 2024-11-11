package com.smartbiz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartbiz.dto.OrderDTO;
import com.smartbiz.service.BuyerOrderService;
import com.smartbiz.service.SellerOrderService;

@RestController
@CrossOrigin
@RequestMapping("/api/seller")
public class SellerOrderController {
	
	@Autowired
	private SellerOrderService sellerService;
	@GetMapping("/{storeId}/orders")
	public ResponseEntity<List<OrderDTO>> getAllOrders(@PathVariable String storeId){
		List<OrderDTO> orders = sellerService.getAllOrders(storeId);
		return new ResponseEntity<>(orders,HttpStatus.OK);
	}
	
	@GetMapping("/{storeId}/orders/order-details")
	public ResponseEntity<OrderDTO> getOrderById(@PathVariable String storeId,@RequestParam String orderId){
		OrderDTO order = sellerService.getOrderById(storeId, orderId);
		return new ResponseEntity<>(order,HttpStatus.OK);
	}
	
}
