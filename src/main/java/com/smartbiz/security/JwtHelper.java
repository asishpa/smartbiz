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
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtHelper {
	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 40;
	private static final String secret = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

	public String getUserIdFromToken(String token) {
		return getClaimFromToken(token, claims -> (String) claims.get("userId"));
	}
	
	public String getUserNameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public List<String> getRolesFromToken(String token) {
		String roles = getClaimFromToken(token, claims -> (String) claims.get("roles"));
		return List.of(roles.split(","));
	}

	public Date getExpirationFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public Boolean isTokenExpired(String token) {
		return getExpirationFromToken(token).before(new Date());
	}

	public String generateToken(String email, List<String> roles, String userId) {
		Map<String, Object> claims = Map.of(
				"userId", userId,
				"roles", String.join(",", roles)
		);

		return createToken(claims, email);
	}

	private String createToken(Map<String, Object> claims, String subject) {
		String token = Jwts.builder().claims(claims).subject(subject).issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(getSignKey(), Jwts.SIG.HS256).compact();
		//System.out.println(token);
		return token;
		
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUserNameFromToken(token);
		final List<String> roles = getRolesFromToken(token);
		System.out.println("Username from token: " + username);
		//System.out.println("roles"+role);
		 // Check if the username matches the UserDetails
	    boolean isUsernameValid = username.equals(userDetails.getUsername());
	    System.out.println("Is username valid: " + isUsernameValid);
	    
	    // Check if the token is expired
	    boolean isTokenExpired = isTokenExpired(token);
	    //System.out.println("Is token expired: " + isTokenExpired);
	    
	    // Check if the role is valid
	    boolean hasValidRole = roles.stream().anyMatch(this::isValidRole);
        //System.out.println("Has valid role: " + hasValidRole);
        return isUsernameValid && !isTokenExpired && hasValidRole;
	}

	private SecretKey getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token).getPayload();
	}

	private boolean isValidRole(String role) {
		return "BUYER".equals(role) || "STORE_OWNER".equals(role);
	}
}
