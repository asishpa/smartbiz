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
import com.smartbiz.entity.Orders.PaymentMethod;
import com.smartbiz.entity.Orders.PaymentStatus;
import com.smartbiz.entity.ProductWarehouseInventory;
import com.smartbiz.exceptions.BusinessException;
import com.smartbiz.exceptions.InsufficientInventoryException;
import com.smartbiz.exceptions.PaymentException;
import com.smartbiz.exceptions.ResourceNotFoundException;
import com.smartbiz.mapper.EntityMapper;
import com.smartbiz.model.CreateOrder;
import com.smartbiz.repository.CartRepository;
import com.smartbiz.repository.InventoryRepo;
import com.smartbiz.repository.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

@Service
public class BuyerOrderServiceImpl implements BuyerOrderService {

	@Autowired
	private CartRepository cartRepo;

	@Autowired
	private InventoryRepo inventoryRepo;

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private StripePaymentService paymentService;

	@Autowired
	private EntityMapper entityMapper;

	@Override
	/**
	 * Configures a transactional boundary with REPEATABLE_READ isolation level.
	 * This ensures that within a single transaction, any data read during the
	 * transaction will remain consistent, even if other concurrent transactions
	 * modify it. Prevents issues like non-repeatable reads, ensuring stable reads
	 * on the same data.
	 */
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public OrderDTO createOrder(String userId, String storeId, CreateOrder createOrder) {

		try {
			Cart cart = validateCart(userId, storeId, false);
			System.out.println("entered after validate");
			if (createOrder.getPaymentMethod() == PaymentMethod.ONLINE) {
				System.out.println("enetered in online mode");
				return handleOnlinePayment(cart);
			} else if (createOrder.getPaymentMethod() == PaymentMethod.COD) {

				return handleCod(cart);
			} else {
				throw new BusinessException("Unsupported Payment method" + createOrder.getPaymentMethod());
			}

		} catch (Exception e) {
			throw new BusinessException(AppConstants.ERROR_ORDER_CREATE_FAIL);
		}

	}

	@Override
	public List<OrderDTO> getAllOrders(String userId, String storeId) {
		List<Orders> orders = orderRepo.findByCustomerUserIdAndStoreId(userId, storeId);
		if (orders.isEmpty()) {
			throw new ResourceNotFoundException(AppConstants.ERROR_ORDER_NOT_FOUND_FOR_STORE);
		}
		return orders.stream().map(entityMapper::toOrderDTO).toList();
	}

	public OrderDTO getOrderById(String userId, String orderId) {
		Orders order = orderRepo.findById(orderId).orElseThrow(() -> {
			throw new ResourceNotFoundException(AppConstants.ERROR_ORDER_NOT_FOUND);
		});
		return entityMapper.toOrderDTO(order);
	}

	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public OrderDTO cancelOrder(String orderId, String reason) {
		Orders order = orderRepo.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_ORDER_NOT_FOUND));
		if (!OrderStatus.PENDING.equals(order.getStatus())) {
			throw new BusinessException("Order cannot be cancelled in current status:" + order.getStatus());
		}

		return null;
	}

	public OrderDTO confirmOrder(String sessionId, CreateOrder createOrder) {
		try {
			boolean isSuccessful = paymentService.verifyPayment(sessionId);
			if (!isSuccessful) {
				throw new PaymentException("Payment unsuccessful");
			}
			Cart cart = cartRepo.findById(createOrder.getCartId())
					.orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_CART_NOT_FOUND));
			String warehouseId = validateAndReserveInventory(cart);
			Orders order = createOrderFromCart(cart, warehouseId);
			order.setPaymentMethod(PaymentMethod.ONLINE);
			order.setPaymentStatus(PaymentStatus.PAID);
			orderRepo.save(order);
			cartRepo.delete(cart);
			return entityMapper.toOrderDTO(order);

		} catch (

		StripeException e) {

			throw new PaymentException("Payment failed" + e.getMessage());
		} catch (Exception e) {
			returnInventoryToStock(null);
			throw new BusinessException(AppConstants.ERROR_ORDER_CREATE_FAIL);
		}
	}

	private OrderDTO handleCod(Cart cart) {
		String warehouseId = validateAndReserveInventory(cart);
		Orders order = createOrderFromCart(cart, warehouseId);
		order.setPaymentMethod(PaymentMethod.COD);
		orderRepo.save(order);
		cartRepo.delete(cart);
		return entityMapper.toOrderDTO(order);
	}

	private OrderDTO handleOnlinePayment(Cart cart) {
		try {
			System.out.println("enetered in online mode1");
			Session checkoutSession = paymentService.createCheckoutSession(cart);
			System.out.println(checkoutSession.getId());
			OrderDTO pendingOrder = new OrderDTO();
			pendingOrder.setCheckoutSessionUrl(checkoutSession.getUrl());
			pendingOrder.setCheckoutSessionId(checkoutSession.getId());
			pendingOrder.setStatus(OrderStatus.PENDING);
			return pendingOrder;
		} catch (Exception e) {
			e.printStackTrace();
			throw new PaymentException("Failed to initalize payment.:" + e.getMessage());
		}

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

	private String validateAndReserveInventory(Cart cart) {
		for (var cartItem : cart.getItems()) {
			List<ProductWarehouseInventory> inventories = inventoryRepo
					.findByProduct_IdAndWarehouse_Store_Id(cartItem.getProduct().getId(), cart.getStore().getId());

			if (inventories.isEmpty()) {
				throw new InsufficientInventoryException(
						"No inventory for product: " + cartItem.getProduct().getProductName());
			}

			int totalAvailable = inventories.stream().mapToInt(ProductWarehouseInventory::getQuantity).sum();
			if (totalAvailable < cartItem.getQuantity()) {
				throw new InsufficientInventoryException(AppConstants.INSUFFICIENT_QUANTITY);
			}

			// Reserve inventory and return the selected warehouse ID
			return reserveInventory(cartItem.getQuantity(), inventories);
		}
		throw new BusinessException("Unable to reserve inventory");

	}

	// delete the entire quantity from one inventory or from multiple
	private String reserveInventory(int requiredQty, List<ProductWarehouseInventory> inventories) {
		int remainingQty = requiredQty;

		for (ProductWarehouseInventory inventory : inventories) {
			if (requiredQty <= 0)
				break;

			int availableQty = inventory.getQuantity();
			int deductQty = Math.min(remainingQty, availableQty);
			inventory.setQuantity(availableQty - deductQty);
			remainingQty -= deductQty;
			inventoryRepo.save(inventory);

			// Return the warehouse ID of the reserved inventory
			return inventory.getWarehouse().getWarehouseId();
		}
		throw new InsufficientInventoryException("Failed to reserve inventory");

	}

	private Orders createOrderFromCart(Cart cart, String warehouseId) {

		Orders order = Orders.builder().orderAmt(cart.getTotal()).status(OrderStatus.PENDING)
				.buyerAddress(cart.getDeliveryAddress()).offer(cart.getAppliedOffer()).customer(cart.getCustomer())
				.store(cart.getStore()).build();
		// Apply the offer's discount if applicable
		if (cart.getAppliedOffer() != null) {
			BigDecimal discount = cart.getAppliedOffer().getMaximumDiscountAmount(); // Assuming 'Offer' has a
																						// discountAmount field
			order.setOffer(cart.getAppliedOffer());
			order.setOrderAmt(order.getOrderAmt().subtract(discount)); // Adjust the total based on the discount
		}

		// create order items using setters
		cart.getItems().forEach(cartItem -> {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setQty(cartItem.getQuantity());
			orderItem.setPrice(cartItem.getPrice());
			orderItem.setSubtotal(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
			// Add the created order item to the order
			order.addItem(orderItem);
		});
		order.updateStatus(OrderStatus.PENDING, "Order Created");
		order.setWarehouseId(warehouseId);
		return order;
	}

	private void returnInventoryToStock(Orders order) {
		order.getItems().forEach(orderItem -> {
			List<ProductWarehouseInventory> inventories = inventoryRepo
					.findByProduct_IdAndWarehouse_Store_Id(orderItem.getProduct().getId(), order.getStore().getId());

			inventories.forEach(inventory -> {
				inventory.setQuantity(inventory.getQuantity() + orderItem.getQty());
				inventoryRepo.save(inventory);
			});
		});
	}

}
