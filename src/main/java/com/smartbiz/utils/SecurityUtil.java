package com.smartbiz.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.smartbiz.entity.User;
import com.smartbiz.repository.UserRepository;
import com.smartbiz.security.JwtHelper;

@Component
public class SecurityUtil {
	@Autowired
	private UserRepository userRepo;

    public String getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userName = userDetails.getUsername();
        String userId = userRepo.findUserIdByUserName(userName);
        System.out.println("userId:"+userId);
        return userId;
    }
}