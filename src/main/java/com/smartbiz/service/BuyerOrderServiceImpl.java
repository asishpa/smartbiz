package com.smartbiz.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.smartbiz.constants.AppConstants;
import com.smartbiz.dto.OrderDTO;
import com.smartbiz.entity.Cart;
import com.smartbiz.entity.OrderItem;
import com.smartbiz.entity.Orders;
import com.smartbiz.entity.Orders.OrderStatus;
import com.smartbiz.entity.ProductWarehouseInventory;
import com.smartbiz.exceptions.BusinessException;
import com.smartbiz.exceptions.InsufficientInventoryException;
import com.smartbiz.exceptions.ResourceNotFoundException;
import com.smartbiz.mapper.EntityMapper;
import com.smartbiz.model.CreateOrder;
import com.smartbiz.repository.CartRepository;
import com.smartbiz.repository.InventoryRepo;
import com.smartbiz.repository.OrderRepository;

@Service
public class BuyerOrderServiceImpl implements BuyerOrderService {

	@Autowired
	private CartRepository cartRepo;

	@Autowired
	private InventoryRepo inventoryRepo;

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private EntityMapper entityMapper;

	@Override
	/**
	 * Configures a transactional boundary with REPEATABLE_READ isolation level.
	 * This ensures that within a single transaction, any data read during the transaction will remain consistent, 
	 * even if other concurrent transactions modify it. 
	 * Prevents issues like non-repeatable reads, ensuring stable reads on the same data.
	 */
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public OrderDTO createOrder(String userId, String storeId, CreateOrder createOrder) {

		try {
			Cart cart = validateCart(userId, storeId, false);
			validateAndReserveInventory(cart);
			System.out.println("enetered afetr validate");
			Orders order = createOrderFromCart(createOrder);
			orderRepo.save(order);
			cartRepo.delete(cart);
			return entityMapper.toOrderDTO(order);
		} catch (Exception e) {
			//System.out.print(e.printStackTrace());
			e.printStackTrace();
			throw new BusinessException(AppConstants.ERROR_ORDER_CREATE_FAIL);
		}

	}
	@Override
	public List<OrderDTO> getOrders(String userId, String storeId) {
		List<Orders> orders = orderRepo.findByCustomerUserIdAndStoreId(userId, storeId);
		if (orders.isEmpty()) {
			throw new ResourceNotFoundException(AppConstants.ERROR_ORDER_NOT_FOUND_FOR_STORE);
		}
		return orders.stream()
				.map(entityMapper::toOrderDTO)
				.toList();
	}
	private Cart validateCart(String userId, String storeId, boolean buynow) {
		Optional<Cart> cartOptional = buynow
				? cartRepo.findByCustomer_UserIdAndStore_IdAndTemporary(userId, storeId, true)
				: cartRepo.findByCustomer_UserIdAndStore_Id(userId, storeId);
		Cart cart = cartOptional.get();

		if (cart.getItems().isEmpty()) {
			throw new ResourceNotFoundException(AppConstants.ERROR_CART_EMPTY);
		}

		if (cart.getDeliveryAddress() == null) {
			throw new BusinessException("Buyer" + AppConstants.ERROR_ADDRESS_NOT_FOUND);
		}
		return cart;
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

	// delete the entire quantity from one inventory or from multiple
	private void reserveInventory(int requiredQty, List<ProductWarehouseInventory> inventories) {
		int remainingQty = requiredQty;

		for (ProductWarehouseInventory inventory : inventories) {
			if (requiredQty <= 0) {
				break;
			}
			// calculate the qty to be reserved
			int availableQty = inventory.getQuantity();
			int deductQty = Math.min(remainingQty, availableQty);
			// deduct the qty from inventory
			inventory.setQuantity(availableQty - deductQty);
			requiredQty -= deductQty;
			// save to db
			inventoryRepo.save(inventory);

		}

	}

	private Orders createOrderFromCart(CreateOrder createOrder) {
		Cart cart = cartRepo.findById(createOrder.getCartId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_CART_NOT_FOUND));
		Orders order = Orders.builder().orderAmt(cart.getTotal()).status(OrderStatus.PENDING)
				.paymentMethod(createOrder.getPaymentMethod()).orderType(createOrder.getFulfillmentType())
				.buyerAddress(cart.getDeliveryAddress()).offer(cart.getAppliedOffer()).customer(cart.getCustomer())
				.store(cart.getStore()).build();
		
		// create order items
		//cart.getItems().forEach(cartItem -> {
			// Debug: Print each cart item details
			/*
			 * System.out.println("Processing Cart Item:");
			 * System.out.println("Product ID: " + cartItem.getProducts().getId());
			 * System.out.println("Product Name: " +
			 * cartItem.getProducts().getProductName()); System.out.println("Quantity: " +
			 * cartItem.getQuantity()); System.out.println("Price per Unit: " +
			 * cartItem.getPrice()); System.out.println("Subtotal for Item: " +
			 * cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
			 */
			// create order items using setters
		    cart.getItems().forEach(cartItem -> {
		        OrderItem orderItem = new OrderItem();
		        orderItem.setOrder(order);
		        orderItem.setProduct(cartItem.getProducts());
		        orderItem.setQty(cartItem.getQuantity());
		        orderItem.setPrice(cartItem.getPrice());
		        orderItem.setSubtotal(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
		        // Add the created order item to the order
		        order.addItem(orderItem);
		});
		order.updateStatus(OrderStatus.PENDING, "Order Created");
		return order;
	}
	
}
