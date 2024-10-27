package com.smartbiz.service;

import java.util.Map;

import com.smartbiz.model.LoginRequest;
import com.smartbiz.model.RegisterBuyer;
import com.smartbiz.model.RegisterSeller;

public interface UserService {
	public boolean sellerSignup(RegisterSeller seller);
	public Map<String, Object> sellerLogin(LoginRequest seller);
	public boolean buyerSignup(RegisterBuyer buyer);
	public Map<String, Object> buyerLogin(LoginRequest buyer);
}
