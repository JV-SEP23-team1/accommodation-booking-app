package com.example.accommodationbookingapp.service.session;

import com.stripe.model.checkout.Session;

import java.math.BigDecimal;

public interface SessionService {
    Session createPaymentSession(BigDecimal bookingPrice);
}
