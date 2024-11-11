package com.smartbiz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartbiz.dto.OrderDTO;
import com.smartbiz.model.CreateOrder;
import com.smartbiz.service.BuyerOrderService;

@RestController
@CrossOrigin
@RequestMapping("/api/buyer")
public class BuyerOrderController {
	@Autowired
	private BuyerOrderService buyerOrderService;

	@PostMapping("/{userId}/orders")
	public ResponseEntity<OrderDTO> createOrder(@PathVariable String userId, @RequestParam String storeId,
			@RequestBody CreateOrder createOrder) {

		OrderDTO order = buyerOrderService.createOrder(userId, storeId, createOrder);
		return new ResponseEntity<>(order, HttpStatus.CREATED);

	}
	@GetMapping("/{userId}/orders")
	public ResponseEntity<List<OrderDTO>> getOrdersByStore(@PathVariable String userId, @RequestParam String storeId) {
	    List<OrderDTO> orders = buyerOrderService.getAllOrders(userId, storeId);
	    return new ResponseEntity<>(orders, HttpStatus.OK);
	}
	@GetMapping("/{userId}/orders/order-details")
	public ResponseEntity<OrderDTO> getOrderById(@PathVariable String userId,@RequestParam String orderId){
		OrderDTO order = buyerOrderService.getOrderById(userId, orderId);
		return new ResponseEntity<>(order,HttpStatus.OK);
	}

}
