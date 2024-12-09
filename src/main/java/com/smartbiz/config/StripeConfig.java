package com.smartbiz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;
import com.stripe.StripeClient;

@Configuration
public class StripeConfig {
	@Value("${stripe.secret_key}")
    private String stripeSecretKey;

    @Bean
    public StripeClient stripeClient() {
        Stripe.apiKey = stripeSecretKey;
        return new StripeClient(stripeSecretKey);
    }
}
