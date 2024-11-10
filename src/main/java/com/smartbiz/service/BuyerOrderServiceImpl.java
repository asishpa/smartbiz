package com.smartbiz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartbiz.constants.AppConstants;
import com.smartbiz.dto.OrderDTO;
import com.smartbiz.entity.Cart;
import com.smartbiz.entity.Orders;
import com.smartbiz.entity.User;
import com.smartbiz.exceptions.ResourceNotFoundException;
import com.smartbiz.mapper.EntityMapper;
import com.smartbiz.model.CreateOrder;
import com.smartbiz.repository.CartRepository;
import com.smartbiz.repository.InventoryRepo;
import com.smartbiz.repository.OrderRepository;
import com.smartbiz.repository.UserRepository;

@Service
public class BuyerOrderServiceImpl implements BuyerOrderService {

	/*
	 * @Autowired private UserRepository userRepo;
	 * 
	 * @Autowired private CartRepository cartRepo;
	 * 
	 * @Autowired private InventoryRepo inventoryRepo;
	 * 
	 * @Autowired private OrderRepository orderRepo;
	 * 
	 * @Autowired private EntityMapper entityMapper;
	 * 
	 * @Override public OrderDTO createOrder(String userId, CreateOrder createOrder)
	 * { User user = userRepo.findById(userId) .orElseThrow(() -> new
	 * ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND)); Cart cart =
	 * cartRepo.findById(createOrder.getCartId()) .orElseThrow(() -> new
	 * ResourceNotFoundException(AppConstants.ERROR_CART_NOT_FOUND));
	 * 
	 * Orders orders = new Orders();
	 * 
	 * orders.setCustomer(user);
	 * 
	 * return null; } private void validateCart(Cart cart) { if
	 * (cart.getItems().isEmpty()) { throw new
	 * ResourceNotFoundException(AppConstants.ERROR_CART_EMPTY); }
	 * 
	 * if (cart.getDeliveryAddress() == null) { throw new
	 * ResourceNotFoundException("Buyer"+AppConstants.ERROR_ADDRESS_NOT_FOUND); } }
	 * private void validateAndReserveInventory(Cart cart) { for(var cartItem :
	 * cart.getItems()) { //List<ProductWarehouseInventory> inventories =
	 * inventoryRepo.findByProduct_IdAndWarehouse_Store_Id(cart.get, null) } }
	 */

}
