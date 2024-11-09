package com.smartbiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartbiz.dto.CartResponseDTO;
import com.smartbiz.model.CartAction;
import com.smartbiz.service.CartService;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/api/users")
public class CartController {

	@Autowired
	private CartService cartService;

	@PostMapping("/{userId}/cart")
	public ResponseEntity<CartResponseDTO> addItemToCart(@PathVariable String userId, @RequestBody CartAction addCart) {
		CartResponseDTO cartResponse = cartService.addItemToCart(userId, addCart);
		return new ResponseEntity<>(cartResponse, HttpStatus.OK);
	}
	@DeleteMapping("/{userId}/cart")
	public ResponseEntity<CartResponseDTO> removeItemFromCart(@PathVariable String userId,@RequestBody CartAction removeItemFromCart,@RequestParam boolean removeCompletely){
		CartResponseDTO cartResponse = cartService.decreaseQuantityFromCart(userId, removeItemFromCart, removeCompletely);
		return new ResponseEntity<>(cartResponse,HttpStatus.OK);
	}
	@GetMapping("/{userId}/cart")
	public ResponseEntity<CartResponseDTO> getCartDetails(@PathVariable String userId,@RequestParam String storeId){
		CartResponseDTO cart = cartService.getCart(userId, storeId);
		return new ResponseEntity<>(cart,HttpStatus.OK);
	}
	@PostMapping("/{userId}/apply-offer")
	public ResponseEntity<CartResponseDTO> applyOfferToCart(@PathVariable String userId,
			@RequestParam String storeId,@RequestParam String offerId){
		CartResponseDTO cart = cartService.applyOffer(userId, storeId, offerId);
		return new ResponseEntity<>(cart,HttpStatus.ACCEPTED);
	}
	@DeleteMapping("/{userId}/remove-offer")
	public ResponseEntity<CartResponseDTO> removeOfferFromCart(String userId,String storeId){
		CartResponseDTO cart = cartService.removeOffer(userId, storeId);
		return new ResponseEntity<>(cart,HttpStatus.OK);
	}
	

}
