package com.example.accommodationbookingapp.service.session;

import com.example.accommodationbookingapp.model.Booking;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;

public interface SessionService {
    Session createPaymentSession(BigDecimal bookingPrice, Booking booking);
}
