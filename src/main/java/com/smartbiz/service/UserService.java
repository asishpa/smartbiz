package com.smartbiz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartbiz.entity.User;
import com.smartbiz.exceptions.UserExistsException;
import com.smartbiz.model.RegisterSeller;
import com.smartbiz.repository.RoleRepository;
import com.smartbiz.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public User registerUser(RegisterSeller request) {
		if (userRepo.findByEmail(request.getEmail()).isPresent()) {
			throw new UserExistsException("User already exists with given email");
		}
		String encodedPwd = passwordEncoder.encode(request.getPassword());
		
		User user = new User();
		user.setUserName(request.getName());
		user.setEmail(request.getEmail());
		user.setEmailVerified(false);
		user.setPassword(encodedPwd);
		return user;
	}
}
