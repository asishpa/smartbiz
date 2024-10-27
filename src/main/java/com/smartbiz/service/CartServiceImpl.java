package com.smartbiz.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.smartbiz.constants.AppConstants;
import com.smartbiz.entity.Cart;
import com.smartbiz.entity.Products;
import com.smartbiz.entity.User;
import com.smartbiz.exceptions.ProductNotFoundException;
import com.smartbiz.exceptions.UserNotFoundException;
import com.smartbiz.model.AddToCart;
import com.smartbiz.repository.CartItemRepository;
import com.smartbiz.repository.CartRepository;
import com.smartbiz.repository.InventoryRepo;
import com.smartbiz.repository.ProductsRepository;
import com.smartbiz.repository.UserRepository;

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
	public Cart addToCart(AddToCart addCart) {
		User customer = userRepo.findById(addCart.getCustomerId()).orElseThrow(() -> new UserNotFoundException(AppConstants.ERROR_USER_NOT_FOUND));
		Products products = productsRepo.findById(addCart.getProductId()).orElseThrow(() -> new ProductNotFoundException(AppConstants.ERROR_PRODUCT_NOT_FOUND));
		
		return null;
	}

}
