package com.smartbiz.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smartbiz.entity.Store;
import com.smartbiz.exceptions.ResourceNotFoundException;
import com.smartbiz.exceptions.UnauthorizedAccessException;
import com.smartbiz.repository.StoreRepository;
import com.smartbiz.utils.SecurityUtil;

@Aspect
@Component
public class SellerAuthorizationAspect {
	
	@Autowired
	private StoreRepository storeRepo;
	
	@Autowired
	private SecurityUtil securityUtil;
	
	@Pointcut("execution(* com.smartbiz.service..*(String, ..)) && args(storeId,..)")
	public void storeOperation(String storeId) {}
	
	 @Before("storeOperation(storeId)")
	    public void checkStoreOwnership(String storeId) {
	        String authenticatedUserId = securityUtil.getAuthenticatedUserId();
	        Store store = storeRepo.findById(storeId).orElseThrow(() -> new ResourceNotFoundException("Store not found"));

	        if (!store.getOwner().getUserId().equals(authenticatedUserId)) {
	            throw new UnauthorizedAccessException("You are not allowed to access this store");
	        }
	    }
}
