package com.smartbiz.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.smartbiz.constants.AppConstants;
import com.smartbiz.entity.Cart;
import com.smartbiz.entity.CartItem;
import com.smartbiz.entity.ProductWarehouseInventory;
import com.smartbiz.entity.Products;
import com.smartbiz.entity.User;
import com.smartbiz.exceptions.InsufficientInventoryException;
import com.smartbiz.exceptions.ProductNotFoundException;
import com.smartbiz.exceptions.UserNotFoundException;
import com.smartbiz.model.AddToCart;
import com.smartbiz.repository.CartItemRepository;
import com.smartbiz.repository.CartRepository;
import com.smartbiz.repository.InventoryRepo;
import com.smartbiz.repository.ProductsRepository;
import com.smartbiz.repository.UserRepository;

import jakarta.transaction.Transactional;

public class CartServiceImpl implements CartService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ProductsRepository productsRepo;
	
	@Autowired
	private InventoryRepo inventoryRepo;
	
	@Autowired
	private CartItemRepository cartItemRepo;
	
	@Autowired
	private CartRepository cartRepo;
	@Override
	@Transactional
	public Cart addToCart(AddToCart addCart) {
		User customer = userRepo.findById(addCart.getCustomerId()).orElseThrow(() -> new UserNotFoundException(AppConstants.ERROR_USER_NOT_FOUND));
		Products products = productsRepo.findById(addCart.getProductId()).orElseThrow(() -> new ProductNotFoundException(AppConstants.ERROR_PRODUCT_NOT_FOUND));
		Integer totalInventory = products.getWarehouseInventories().stream()
						.mapToInt(ProductWarehouseInventory::getQuantity)
						.sum();
		if (addCart.getQuantity() > totalInventory) {
			throw new InsufficientInventoryException(AppConstants.INSUFFICIENT_QUANTITY);
		}
		Cart cart = cartRepo.findByCustomerUserId(addCart.getCustomerId()).orElseGet(() -> {
			Cart newCart = new Cart();
			newCart.setCustomer(customer);
			return cartRepo.save(newCart);
		});
		CartItem existingItem = cart.getItems().stream()
				.filter(item -> item.getProducts().getId().equals(addCart.getProductId()))
				.findFirst()
				.orElse(null);
		if (existingItem != null) {
			int newQuantity = existingItem.getQuantity()+addCart.getQuantity();
			if (newQuantity > totalInventory) {
				throw new InsufficientInventoryException(AppConstants.INSUFFICIENT_QUANTITY);
			}
			existingItem.setQuantity(existingItem.getQuantity()+addCart.getQuantity());
		}		
		else {
			CartItem cartItem = new CartItem();
			cartItem.setProducts(products);
			cartItem.setQuantity(addCart.getQuantity());
			cartItem.setCart(cart);
			cart.getItems().add(cartItem);
			
		}
		return cartRepo.save(cart);
	}

}
