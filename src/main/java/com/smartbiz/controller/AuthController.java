package com.smartbiz.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartbiz.exceptions.UserExistsException;
import com.smartbiz.model.LoginSeller;
import com.smartbiz.model.RegisterSeller;
import com.smartbiz.service.UserService;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/seller/register")
	public ResponseEntity<?> registerSeller(@RequestBody RegisterSeller request){
		try {
			userService.sellerSignup(request);
			return ResponseEntity.ok("User Registerd successfully");
		} catch (UserExistsException e) {
			return ResponseEntity.status(409).body(e.getMessage());
		}
	}
	@PostMapping("/seller/login")
	public ResponseEntity<?> loginSeller(@RequestBody LoginSeller request){
		System.out.println(request);
		try {
			
			Map<String, Object> response = userService.sellerLogin(request);
			if ("INVALID_CRED".equals(response.get("status"))) {
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			}
			else {
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
