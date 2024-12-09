package com.smartbiz.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.smartbiz.entity.Cart;
import com.smartbiz.entity.CartItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class StripePaymentService {

    @Value("${stripe.secret_key}")
    private String secretKey;

    public Session createCheckoutSession(Cart cart) throws StripeException {
        // Set the Stripe secret key
        Stripe.apiKey = secretKey;

        // Create line items for the checkout session
        List<SessionCreateParams.LineItem> lineItems = cart.getItems().stream()
            .map(this::createLineItem)
            .collect(Collectors.toList());

        // Create session params for Stripe
        SessionCreateParams params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("http://yourdomain.com/api/payment/success")
            .setCancelUrl("http://yourdomain.com/api/payment/cancel")
            .addAllLineItem(lineItems)
            .build();

        // Create and return the session
        return Session.create(params);
    }

    private SessionCreateParams.LineItem createLineItem(CartItem cartItem) {
        // Convert price to cents (Stripe requires amount in smallest currency unit)
        long unitAmount = convertToCents(cartItem.getPrice());

        return SessionCreateParams.LineItem.builder()
            .setQuantity(Long.valueOf(cartItem.getQuantity()))
            .setPriceData(
                SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency("INR")
                    .setUnitAmount(unitAmount)
                    .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(cartItem.getProduct().getProductName())
                            .build()
                    )
                    .build()
            )
            .build();
    }

    // Helper method to convert BigDecimal to cents
    private long convertToCents(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(100)).longValue();
    }
    public boolean verifyPayment(String sessionId) throws StripeException {
        Session session = Session.retrieve(sessionId);
        return "paid".equals(session.getPaymentStatus());
    }
}