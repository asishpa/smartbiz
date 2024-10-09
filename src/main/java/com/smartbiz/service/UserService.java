package com.smartbiz.service;

import java.util.Map;

import com.smartbiz.model.LoginSeller;
import com.smartbiz.model.RegisterSeller;

public interface UserService {
	public boolean sellerSignup(RegisterSeller seller);
	public Map<String, Object> sellerLogin(LoginSeller seller);
}
