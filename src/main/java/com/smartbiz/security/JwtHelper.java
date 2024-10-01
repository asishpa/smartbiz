package com.smartbiz.security;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtHelper {
	public static final long JWT_TOKEN_VALIDITY = 5*60*40;
	private static final String secret = "asish";
	
	public String getUserNameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	public String getRoleFromToken(String token) {
		return getClaimFromToken(token, claims -> (String) claims.get("role"));
	}
	public Date getExpirationFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	public Boolean isTokenExpired(String token) {
		return getExpirationFromToken(token).before(new Date());
	}
	public String generateToken(String userName,String roleName,String userId) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", roleName);
		claims.put("userId", userId);
		return createToken(claims, userName);
	}
	private String createToken(Map<String, Object> claims,String subject) {
		
		return Jwts.builder()
				.claims(claims)
				.subject(subject)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis()+ JWT_TOKEN_VALIDITY * 1000))
				.signWith(getSignKey(), Jwts.SIG.HS256)
				.compact();
	}
	public Boolean validateToken(String token,UserDetails userDetails) {
		final String username = getUserNameFromToken(token);
		final String role = getRoleFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && isValidRole(role));
	}
	private SecretKey getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	private <T> T getClaimFromToken(String token,Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token).getPayload();
	}
	private boolean isValidRole(String role) {
		return "customer".equals(role) || "store_owner".equals(role); 
	}
}
