package com.smartbiz.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.smartbiz.security.CustomUserDetailsService;
import com.smartbiz.security.JwtAuthenticationEntryPoint;
import com.smartbiz.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtAuthenticationEntryPoint entryPoint;
	private final JwtAuthenticationFilter filter;
	private final CustomUserDetailsService userDetailsService;

	public SecurityConfig(JwtAuthenticationEntryPoint entryPoint, JwtAuthenticationFilter filter,
			CustomUserDetailsService userDetailsService) {
		this.entryPoint = entryPoint;
		this.filter = filter;
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.configurationSource(corsConfigurationSource())).csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(requests -> requests.requestMatchers("/api/auth/**").permitAll()
						.requestMatchers("/api-docs/**").permitAll().requestMatchers("/swagger-ui/**").permitAll()
						.requestMatchers("/api/media/**").permitAll().requestMatchers("/api/stores/public/**")
						.permitAll().anyRequest().authenticated())
				.exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Apply JWT filter only to protected routes
		http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "https://store-project-iota.vercel.app","http://localhost:3000","https://buysync.vercel.app"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH")); // Specify allowed
																									// methods
		configuration.setAllowCredentials(true); // Allow cookies to be sent
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // Specify allowed headers

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration); // Apply CORS configuration to all routes

		return source;
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManager.class);
	}
}