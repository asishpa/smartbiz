package com.smartbiz.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.smartbiz.constants.AppConstants;
import com.smartbiz.dto.BuyerAddressDTO;
import com.smartbiz.dto.CartItemDTO;
import com.smartbiz.dto.CartResponseDTO;
import com.smartbiz.dto.OfferDTO;
import com.smartbiz.entity.BuyerAddress;
import com.smartbiz.entity.Cart;
import com.smartbiz.entity.CartItem;
import com.smartbiz.entity.Delivery;
import com.smartbiz.entity.Delivery.DELIVERY_CHARGE_TYPE;
import com.smartbiz.entity.DeliveryRange;
import com.smartbiz.entity.Offer;
import com.smartbiz.entity.Offer.OfferType;
import com.smartbiz.entity.ProductWarehouseInventory;
import com.smartbiz.entity.Products;
import com.smartbiz.entity.Store;
import com.smartbiz.entity.User;
import com.smartbiz.exceptions.InsufficientInventoryException;
import com.smartbiz.exceptions.InvalidOfferException;
import com.smartbiz.exceptions.ResourceNotFoundException;
import com.smartbiz.mapper.EntityMapper;
import com.smartbiz.model.CartAction;
import com.smartbiz.repository.BuyerAddressRepository;
import com.smartbiz.repository.CartItemRepository;
import com.smartbiz.repository.CartRepository;
import com.smartbiz.repository.InventoryRepo;
import com.smartbiz.repository.OfferRepository;
import com.smartbiz.repository.ProductsRepository;
import com.smartbiz.repository.StoreRepository;
import com.smartbiz.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private StoreRepository storeRepo;

	@Autowired
	private ProductsRepository productsRepo;

	@Autowired
	private InventoryRepo inventoryRepo;

	@Autowired
	private CartItemRepository cartItemRepo;

	@Autowired
	private CartRepository cartRepo;

	@Autowired
	private OfferRepository offerRepo;

	@Autowired
	private BuyerAddressRepository addressRepo;

	@Autowired
	private EntityMapper entityMapper;

	@Override
	@Transactional
	public CartResponseDTO addItemToCart(String userId, CartAction addCart) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND));
		Products product = productsRepo.findById(addCart.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_PRODUCT_NOT_FOUND));
		Store store = storeRepo.findById(addCart.getStoreId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_STORE_NOT_FOUND));
		if (!product.getStore().getId().equals(addCart.getStoreId())) {
			throw new ResourceNotFoundException("Product not in this store");
		}
		Cart cart = getOrCreateCart(user, store);
		CartItem cartItem = getOrCreateCartItem(cart, product);

		validateInventory(addCart.getProductId(), addCart.getStoreId(), cartItem.getQuantity() + 1);
		cartItem.setQuantity(cartItem.getQuantity() + 1);
		cartItemRepo.save(cartItem);
		updateCartTotals(cart);

		return createCartResponse(cart);
	}

	private Cart getOrCreateCart(User user, Store store) {
		// Retrieve or create a new cart for the user and store
		return cartRepo.findByCustomer_UserIdAndStore_Id(user.getUserId(), store.getId()).orElseGet(() -> {
			Cart newCart = new Cart();
			newCart.setCustomer(user);
			newCart.setStore(store);
			return cartRepo.save(newCart);
		});
	}

	private CartItem getOrCreateCartItem(Cart cart, Products product) {
		// Retrieve or create a new cart item for the product in the cart
		return cart.getItems().stream().filter(item -> item.getProducts().getId().equals(product.getId())).findFirst()
				.orElseGet(() -> {
					CartItem newItem = new CartItem();
					newItem.setCart(cart);
					newItem.setProducts(product);
					newItem.setPrice(BigDecimal.valueOf(product.getDiscountedPrice()));
					newItem.setQuantity(0); // Initial quantity set to 0, will increment later
					cart.getItems().add(newItem);
					return newItem;
				});
	}

	public CartResponseDTO decreaseQuantityFromCart(String userId,CartAction removeItemFromCart,boolean removeCompletely) {
		Cart cart = cartRepo.findByCustomer_UserIdAndStore_Id(userId, removeItemFromCart.getStoreId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_CART_NOT_FOUND));
		CartItem cartItem = cart.getItems().stream().filter(item -> item.getProducts().getId().equals(removeItemFromCart.getProductId()))
				.findFirst().orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_PRODUCT_NOT_FOUND));
		if (removeCompletely || cartItem.getQuantity() == 1) {
	        cart.getItems().remove(cartItem);
	        cartItemRepo.delete(cartItem);
	    } else {
	        cartItem.setQuantity(cartItem.getQuantity() - 1);
	        cartItemRepo.save(cartItem);
	    }

		updateCartTotals(cart);
		return createCartResponse(cart);
	}

	public CartResponseDTO applyOffer(String userId, String storeId, String offerId) {
		Offer offer = offerRepo.findById(offerId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_OFFER_NOT_FOUND));
		Cart cart = cartRepo.findByCustomer_UserIdAndStore_Id(userId, storeId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_CART_NOT_FOUND));
		validateOffer(offer, cart);

		cart.setAppliedOffer(offer);
		updateCartTotals(cart);
		return createCartResponse(cart);
	}

	public CartResponseDTO addAddressToCart(String userId, String storeId, Long addressId) {
		Cart cart = cartRepo.findByCustomer_UserIdAndStore_Id(userId, storeId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_CART_NOT_FOUND));

		BuyerAddress address = addressRepo.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_ADDRESS_NOT_FOUND));

		if (address.getCustomer().getUserId().equals(userId)) {
			throw new ResourceNotFoundException(AppConstants.ERROR_ADDRESS_NOT_FOUND);

		}

		cart.setDeliveryAddress(address);
		cartRepo.save(cart);

		return createCartResponse(cart);
	}

	private BigDecimal calculateDeliveryFee(Store store, BigDecimal subTotal) {
		Delivery delivery = store.getDelivery();
		if (delivery == null) {
			return BigDecimal.ZERO;

		}
		if (delivery.getChargeType() == DELIVERY_CHARGE_TYPE.FIXED) {
			if (subTotal.compareTo(BigDecimal.valueOf(delivery.getFreeDeliveryAbove())) >= 0) {
				return BigDecimal.ZERO;

			}
			return BigDecimal.valueOf(delivery.getChargePerOrder());

		} else {
			for (DeliveryRange range : delivery.getVariableCharges()) {
				if (subTotal.compareTo(BigDecimal.valueOf(range.getStartAmt())) >= 0
						&& subTotal.compareTo(BigDecimal.valueOf(range.getEndAmt())) < 0) {
					return BigDecimal.valueOf(range.getCharge());

				}

			}
		}
		return BigDecimal.ZERO;
	}

	private void validateOffer(Offer offer, Cart cart) {
		if (!offer.isActive() || !offer.getStore().getId().equals(cart.getStore().getId())) {
			throw new InvalidOfferException(AppConstants.OFFER_NOT_APPLICABLE_AT_STORE);

		}
		LocalDate now = LocalDate.now();
		System.out.println("now" + now);

		if (now.isBefore(offer.getStartDate())) {
			throw new InvalidOfferException(AppConstants.ERROR_OFFER_NOT_FOUND);
		}
		if (offer.getEndDate() != null && now.isAfter(offer.getEndDate())) {
			throw new InvalidOfferException(AppConstants.ERROR_OFFER_NOT_FOUND);
		}
		BigDecimal subtotal = cart.getSubtotal();
		System.out.println("get subtotal" + subtotal);
		System.out.println("get minimum purchas amount" + offer.getMinimumPurchaseAmount());
		if (subtotal.compareTo(offer.getMinimumPurchaseAmount()) < 0) {
			System.out.println("enetered in 3rd");
			throw new InvalidOfferException(AppConstants.OFFER_NOT_APPLICABLE_AT_CART);
		}

	}

	private void updateCartTotals(Cart cart) {
		BigDecimal subTotal = cart.getItems().stream()
				.map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		cart.setSubtotal(subTotal);
		cart.setDiscountAmount(calculateDiscount(cart));
		cart.setDeliveryCharge(calculateDeliveryFee(cart.getStore(), subTotal));
		cart.setTotal(subTotal.subtract(cart.getDiscountAmount()).add(cart.getDeliveryCharge()));
	}

	private BigDecimal calculateDiscount(Cart cart) {
		Offer offer = cart.getAppliedOffer();
		if (offer == null) {
			return BigDecimal.ZERO;
		}
		BigDecimal discount;
		if (offer.getOfferType() == OfferType.PERCENTAGE_DISCOUNT) {
			discount = cart.getSubtotal().multiply(offer.getPercentageValue()).divide(BigDecimal.valueOf(100));
		} else {
			discount = offer.getFlatAmountValue();
		}
		return discount.min(offer.getMaximumDiscountAmount());
	}

	private void validateInventory(String productId, String storeId, Integer quantity) {
		List<ProductWarehouseInventory> inventories = inventoryRepo.findByProduct_IdAndWarehouse_Store_Id(productId,
				storeId);
		if (inventories.isEmpty()) {
			throw new InsufficientInventoryException(AppConstants.INSUFFICIENT_QUANTITY);
		}
		int totalAvailableQty = inventories.stream().mapToInt(ProductWarehouseInventory::getQuantity).sum();
		if (totalAvailableQty < quantity) {
			throw new InsufficientInventoryException(AppConstants.INSUFFICIENT_QUANTITY);
		}
	}

	private CartResponseDTO createCartResponse(Cart cart) {
		return CartResponseDTO.builder().cartId(cart.getId())
				.items(cart.getItems().stream().map(this::mapToCartItemDTO).collect(Collectors.toList()))
				.subTotal(cart.getSubtotal()).discount(cart.getDiscountAmount())
				.deliveryCharge(cart.getDeliveryCharge()).total(cart.getTotal())
				.appliedOfferId(cart.getAppliedOffer() != null ? mapToOfferDTO(cart.getAppliedOffer()) : null)
				.deliveryAddress(cart.getDeliveryAddress() != null ? mapToAddressDTO(cart.getDeliveryAddress()) : null)
				.build();
	}

	private CartItemDTO mapToCartItemDTO(CartItem cartItem) {
		return entityMapper.toCartItemDTO(cartItem);
	}

	private OfferDTO mapToOfferDTO(Offer offer) {
		return entityMapper.toOfferDTO(offer);
	}

	private BuyerAddressDTO mapToAddressDTO(BuyerAddress address) {
		return entityMapper.toAddressDto(address);
	}

	@Override
	public CartResponseDTO getCart(String userId, String storeId,boolean buyNow) {
		Cart cart;
		if (buyNow) {
			cart = cartRepo.findByCustomer_UserIdAndStore_IdAndIsTemporary(userId, storeId, true)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_CART_NOT_FOUND));
			
		}
		else {
			cart = cartRepo.findByCustomer_UserIdAndStore_Id(userId, storeId)
					.orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_CART_NOT_FOUND));
		}
		

		return entityMapper.toCartResponseDTO(cart);
	}

	@Override
	public CartResponseDTO removeOffer(String userId, String storeId) {
		Cart cart = cartRepo.findByCustomer_UserIdAndStore_Id(userId, storeId)
		.orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_CART_NOT_FOUND));
		if (cart.getAppliedOffer() != null) {
			cart.setAppliedOffer(null);
			updateCartTotals(cart);
			cartRepo.save(cart);
		}
		return createCartResponse(cart);
	}

	@Override
	public CartResponseDTO buyNow(String userId, CartAction cartAction) {
		User user = userRepo.findById(userId)
			.orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_USER_NOT_FOUND));
		Products product = productsRepo.findById(cartAction.getProductId())
			.orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_PRODUCT_NOT_FOUND));
		Store store = storeRepo.findById(cartAction.getStoreId()).orElseThrow(() -> new ResourceNotFoundException(AppConstants.ERROR_STORE_NOT_FOUND));
		if (!product.getStore().getId().equals(cartAction.getStoreId())) {
			throw new ResourceNotFoundException("Product not in this store");
		}
		//validate inventory
		validateInventory(cartAction.getProductId(), cartAction.getStoreId(), 1);
		Cart tempCart = cartRepo.findByCustomer_UserIdAndStore_IdAndIsTemporary(userId, store.getId(), true)
			.orElseGet(() -> {
				Cart newCart = new Cart();
				newCart.setCustomer(user);
				newCart.setStore(store);
				newCart.setTemporary(false);
				return cartRepo.save(newCart);
			});
		if (!tempCart.getItems().isEmpty()) {
			CartItem existingItem = tempCart.getItems().iterator().next();
			tempCart.getItems().remove(existingItem);
			cartItemRepo.delete(existingItem);
		}
		CartItem newItem = new CartItem();
		newItem.setCart(tempCart);
		newItem.setProducts(product);
		newItem.setPrice(BigDecimal.valueOf(product.getDiscountedPrice()));
		newItem.setQuantity(1);
		tempCart.getItems().add(newItem);
		cartItemRepo.save(newItem);
		
		updateCartTotals(tempCart);
		cartRepo.save(tempCart);
		
		return createCartResponse(tempCart);
	}
}
