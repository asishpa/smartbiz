package com.smartbiz.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartbiz.constants.AppConstants;
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
import com.smartbiz.repository.StoreRepository;
import com.smartbiz.repository.UserRepository;
import com.smartbiz.security.JwtHelper;

@Service
public class UserServiceImpl implements UserService {

	private static final Random RANDOM = new Random();
	@Autowired
	private JwtHelper jwtHelper;
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private StoreRepository storeRepo;

	@Override
	@Transactional
	public boolean sellerSignup(RegisterSeller request) throws UserExistsException {
		// Check if user already exists
		if (userRepo.findByEmail(request.getEmail()).isPresent()) {
			throw new UserExistsException(AppConstants.ERROR_USER_EXISTS);
		}

		User user = new User();
		user.setUserName(request.getName());
		user.setEmail(request.getEmail());
		user.setEmailVerified(false);
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		Role storeOwnerRole = roleRepo.findByRoleName("STORE_OWNER")
				.orElseThrow(() -> new RoleNotFoundException(AppConstants.ERROR_ROLE_NOT_FOUND));
		user.getRoles().add(storeOwnerRole);

		Store store = new Store();
		store.setName(request.getStoreName());
		store.setStoreLink(generateUniqueStoreLink(request.getStoreName()));
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
		User user = userRepo.findByEmail(request.getEmail())
				.orElseThrow(() -> new InvalidCredentialsException(AppConstants.INVALID_CRED));
		System.out.println(user);
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new InvalidCredentialsException(AppConstants.INVALID_CRED);
		}
		List<String> roles = userRepo.findRoleNamesByEmail(request.getEmail());
		if (!roles.contains("STORE_OWNER")) {
			throw new UnauthorizedAccessException(AppConstants.ERROR_UNAUTHORIZED_ACCESS);
		}
		String token = jwtHelper.generateToken(user.getEmail(), roles.get(0), String.valueOf(user.getUserId()));
		System.out.println("entered");
		response.put("status", AppConstants.SUCCESS);
		response.put("token", token);
		response.put("roles", roles);
		response.put("owner_name", user.getUserName());
		response.put("store_name", user.getStore().getName());
		response.put("store_id", user.getStore().getId());
		return response;
	}

	private String generateUniqueStoreLink(String storeName) {
		String baseLink = storeName.replaceAll("\\s+", "").toLowerCase();
		String storeLink = baseLink;

		while (storeRepo.findByStoreLink(storeLink).isPresent()) {
			// Append a random number and recheck
			int randomNumber = 1000 + RANDOM.nextInt(900);
			storeLink = baseLink + randomNumber;
		}
		return storeLink;
	}

}