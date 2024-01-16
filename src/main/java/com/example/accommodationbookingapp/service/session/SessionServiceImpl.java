package com.example.accommodationbookingapp.service.session;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private static final String ERROR_CREATING_PAYMENT_SESSION
            = "Error creating payment session";
    public static final String USD = "usd";
    public static final long QUANTITY = 1L;
    private UriComponentsBuilder uriComponentsBuilder;
    @Value("${stripe.domainUrl}")
    private String stripeDomainUrl;
    @Override
    public Session createPaymentSession(BigDecimal bookingPrice) {
        try {
            String successUrl = uriComponentsBuilder.path("/payments/success").build().toUriString();
            String cancelUrl = uriComponentsBuilder.path("/payments/cancel").build().toUriString();

            Long priceInCents = bookingPrice.multiply(BigDecimal.valueOf(100)).longValue();

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(cancelUrl)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(QUANTITY)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency(USD)
                                                    .setUnitAmount(priceInCents)
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();
            return Session.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(ERROR_CREATING_PAYMENT_SESSION, e);
        }
    }
}
