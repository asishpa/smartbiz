package com.smartbiz.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartbiz.entity.Role;
import com.smartbiz.entity.Store;
import com.smartbiz.entity.User;
import com.smartbiz.exceptions.InvalidCredentialsException;
import com.smartbiz.exceptions.RoleNotFoundException;
import com.smartbiz.exceptions.UnauthorizedAccessException;
import com.smartbiz.exceptions.UserExistsException;
import com.smartbiz.model.LoginSeller;
import com.smartbiz.model.RegisterSeller;
import com.smartbiz.repository.RoleRepository;
import com.smartbiz.repository.UserRepository;
import com.smartbiz.security.JwtHelper;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private JwtHelper jwtHelper;
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;


	@Override
	@Transactional
	public boolean sellerSignup(RegisterSeller request) throws UserExistsException {
		// Check if user already exists
		if (userRepo.findByEmail(request.getEmail()).isPresent()) {
			throw new UserExistsException("User with this email already exists");
		}

		User user = new User();
		user.setUserName(request.getName());
		user.setEmail(request.getEmail()); // Fixed: was setting password instead of email
		user.setEmailVerified(false);
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		Role storeOwnerRole = roleRepo.findByRoleName("STORE_OWNER")
				.orElseThrow(() -> new RoleNotFoundException("Role not found"));
		user.getRoles().add(storeOwnerRole);

		Store store = new Store();
		store.setName(request.getStoreName()); // Assuming you have a store name in RegisterSeller
		store.setOwner(user);
		user.setStore(store);

		try {
			userRepo.save(user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> sellerLogin(LoginSeller request) {
	    Map<String, Object> response = new HashMap<>();
	    User user = userRepo.findByEmail(request.getEmail()).orElseThrow(() -> new InvalidCredentialsException("Invalid Credentials"));
	    System.out.println(user);
	    if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
	    	throw new InvalidCredentialsException("Invalid Credentials");
	    }
	    System.out.println("enetered2");
	    List<String> roles = userRepo.findRoleNamesByEmail(request.getEmail());
	    //System.out.println(roles);
	    if (!roles.contains("STORE_OWNER")) {
	    	throw new UnauthorizedAccessException("User is not registered as a seller");
		}
	    String token = jwtHelper.generateToken(user.getEmail(), roles.get(0), String.valueOf(user.getUserId())); 
	    System.out.println("entered");
	    response.put("status", "success");
	    response.put("token", token);
	    response.put("roles", roles);
	    response.put("owner_name", user.getUserName());
	    response.put("store_name", user.getStore().getName());
	    return response;
	}



}