package com.example.accommodationbookingapp.service.session.impl;

import com.example.accommodationbookingapp.model.Booking;
import com.example.accommodationbookingapp.service.session.SessionService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private static final String ERROR_CREATING_PAYMENT_SESSION
            = "Error creating payment session";
    private static final String USD = "usd";
    private static final long QUANTITY = 1L;
    private static final String SUCCESS = "/success";
    private static final String CANCEL = "/cancel";
    private static final String SESSION_ID_PARAM =
            "?session_id={CHECKOUT_SESSION_ID}";
    @Value("${stripe.domainUrl}")
    private String stripeDomainUrl;
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Override
    public Session createPaymentSession(BigDecimal bookingPrice, Booking booking) {
        Stripe.apiKey = stripeApiKey;
        try {
            Long priceInCents = bookingPrice.multiply(BigDecimal.valueOf(100)).longValue();
            Product product = createProduct(booking.getAccommodation().getType().toString());
            Price price = createPrice(priceInCents, product.getId());

            String successUrl = UriComponentsBuilder.fromUriString(stripeDomainUrl)
                    .path(SUCCESS)
                    .path(SESSION_ID_PARAM)
                    .build().toUriString();

            String cancelUrl = UriComponentsBuilder.fromUriString(stripeDomainUrl)
                    .path(CANCEL)
                    .path(SESSION_ID_PARAM)
                    .build().toUriString();

            SessionCreateParams.Builder sessionParamsBuilder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(cancelUrl)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setPrice(price.getId())
                                    .setQuantity(QUANTITY)
                                    .build()
                    );

            return Session.create(sessionParamsBuilder.build());
        } catch (StripeException e) {
            throw new RuntimeException(ERROR_CREATING_PAYMENT_SESSION, e);
        }
    }

    private Product createProduct(String productName) throws StripeException {
        ProductCreateParams productParams = ProductCreateParams.builder()
                .setName(productName)
                .build();
        return Product.create(productParams);
    }

    private Price createPrice(Long unitAmount, String productId) throws StripeException {
        PriceCreateParams priceParams = PriceCreateParams.builder()
                .setCurrency(SessionServiceImpl.USD)
                .setUnitAmount(unitAmount)
                .setProduct(productId)
                .build();
        return Price.create(priceParams);
    }
}
