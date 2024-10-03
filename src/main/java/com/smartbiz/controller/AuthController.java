package com.smartbiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartbiz.entity.User;
import com.smartbiz.exceptions.UserExistsException;
import com.smartbiz.model.RegisterUser;
import com.smartbiz.repository.RoleRepository;
import com.smartbiz.repository.UserRepository;
import com.smartbiz.security.JwtHelper;
import com.smartbiz.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody RegisterUser request){
		try {
			userService.registerUser(request);
			return ResponseEntity.ok("User Registerd successfully");
		} catch (UserExistsException e) {
			return ResponseEntity.status(409).body(e.getMessage());
		}
	}
}
