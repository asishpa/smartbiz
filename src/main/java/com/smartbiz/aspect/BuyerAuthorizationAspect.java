package com.smartbiz.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smartbiz.exceptions.UnauthorizedAccessException;
import com.smartbiz.utils.SecurityUtil;

@Aspect
@Component
public class BuyerAuthorizationAspect {
	
	@Autowired
	private SecurityUtil securityUtil;
	
	@Pointcut("execution(* com.smartbiz.service..*(String, ..)) && args(userId,..)")
	public void buyerOperation(String userId) {}
	
	public void checkBuyerAuthorization(String userId) {
		
		String authenticatedUserId = securityUtil.getAuthenticatedUserId();
		System.out.println("authenticated"+authenticatedUserId);
		if (!authenticatedUserId.equals(userId)) {
			throw new UnauthorizedAccessException("You are not authorized to access this resource");
			
		}
	}
	 
}
