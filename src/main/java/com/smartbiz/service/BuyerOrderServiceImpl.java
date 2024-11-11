package com.smartbiz.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartbiz.constants.AppConstants;
import com.smartbiz.dto.OrderDTO;
import com.smartbiz.entity.Cart;
import com.smartbiz.entity.Orders;
import com.smartbiz.entity.ProductWarehouseInventory;
import com.smartbiz.entity.User;
import com.smartbiz.exceptions.InsufficientInventoryException;
import com.smartbiz.exceptions.ResourceNotFoundException;
import com.smartbiz.mapper.EntityMapper;
import com.smartbiz.model.CreateOrder;
import com.smartbiz.repository.CartRepository;
import com.smartbiz.repository.InventoryRepo;
import com.smartbiz.repository.OrderRepository;
import com.smartbiz.repository.UserRepository;

@Service
public class BuyerOrderServiceImpl implements BuyerOrderService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CartRepository cartRepo;

	@Autowired
	private InventoryRepo inventoryRepo;

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private EntityMapper entityMapper;

	@Override
	public OrderDTO createOrder(String userId, CreateOrder createOrder) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND));
		Cart cart = cartRepo.findById(createOrder.getCartId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_CART_NOT_FOUND));
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		Orders order = new Orders();

		order.setCustomer(user);

		return null;
	}
	
	private void validateCart(String userId,String storeId,boolean buynow) {
		Optional<Cart> cart= buynow ? 
				cartRepo.findByCustomer_UserIdAndStore_IdAndTemporary(userId, storeId,true) :
				cartRepo.findByCustomer_UserIdAndStore_Id(userId, storeId);	
		
		/*
		 * if (cart.getItems().isEmpty()) { throw new
		 * ResourceNotFoundException(AppConstants.ERROR_CART_EMPTY); }
		 */

		/*
		 * if (cart.getDeliveryAddress() == null) { throw new
		 * ResourceNotFoundException("Buyer" + AppConstants.ERROR_ADDRESS_NOT_FOUND); }
		 */
	}

	private void validateAndReserveInventory(Cart cart) {
		for (var cartItem : cart.getItems()) {
			List<ProductWarehouseInventory> inventories = inventoryRepo
					.findByProduct_IdAndWarehouse_Store_Id(cartItem.getProducts().getId(), cart.getStore().getId());
			if (inventories.isEmpty()) {
				throw new InsufficientInventoryException(
						"No inventory for product:" + cartItem.getProducts().getProductName());
			}
			int totalAvailable = inventories.stream().mapToInt(ProductWarehouseInventory::getQuantity).sum();
			if (totalAvailable < cartItem.getQuantity()) {
				throw new InsufficientInventoryException(AppConstants.INSUFFICIENT_QUANTITY);
			}
			reserveInventory(cartItem.getQuantity(), inventories);
		}
		
	}
	//delete the entire quantity from one inventory or from multiple
	private void reserveInventory(int requiredQty,List<ProductWarehouseInventory> inventories) {
		int remainingQty = requiredQty;
		
		for(ProductWarehouseInventory inventory : inventories) {
			if (requiredQty <= 0) {
				break;
			}
			//calculate the qty to be reserved
			int availableQty = inventory.getQuantity();
			int deductQty = Math.min(remainingQty, availableQty);
			//deduct the qty from inventory
			inventory.setQuantity(availableQty - deductQty);
			requiredQty -= deductQty;
			//save to db
			inventoryRepo.save(inventory);
			
		}
	}
}
