package com.example.accommodationbookingapp.service.payment.impl;

import com.example.accommodationbookingapp.dto.payment.PaymentResponseDto;
import com.example.accommodationbookingapp.mapper.PaymentMapper;
import com.example.accommodationbookingapp.model.Booking;
import com.example.accommodationbookingapp.model.Payment;
import com.example.accommodationbookingapp.repository.booking.BookingRepository;
import com.example.accommodationbookingapp.repository.payment.PaymentRepository;
import com.example.accommodationbookingapp.service.date.DateService;
import com.example.accommodationbookingapp.service.payment.PaymentService;
import com.example.accommodationbookingapp.service.session.SessionService;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final String DIDNT_FIND_PAYMENT_WITH_SESSION_ID =
            "Didn't find Payment with SessionId:";
    private static final String DIDNT_FIND_BOOKING_WITH_ID =
            "Didn't find Booking with ID:";
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final SessionService sessionService;
    private final BookingRepository bookingRepository;
    private final DateService dateService;

    @Override
    public List<PaymentResponseDto> findAllByUserId(Long userId) {
        List<Payment> paymentsFromDb = paymentRepository.findAllByUserId(userId);
        return paymentMapper.paymentsToPaymentResponseDtos(paymentsFromDb);
    }

    @Override
    public PaymentResponseDto create(Long bookingId) throws MalformedURLException {
        Booking booking = bookingRepository.findBookingWithAccommodationById(bookingId).orElseThrow(
                () -> new RuntimeException(DIDNT_FIND_BOOKING_WITH_ID + bookingId));
        long numberOfDays = dateService.calculateDurationInDays(
                booking.getCheckInDate(), booking.getCheckOutDate());
        BigDecimal dailyRate = booking.getAccommodation().getDailyRate();
        BigDecimal totalCost = dailyRate.multiply(BigDecimal.valueOf(numberOfDays));

        Session session = sessionService.createPaymentSession(totalCost);

        Payment createdPayment = buildPayment(session, bookingId, totalCost);
        Payment savedPayment = paymentRepository.save(createdPayment);
        return paymentMapper.paymentToPaymentResponseDto(savedPayment);
    }

    @Override
    public PaymentResponseDto update(String sessionId, Payment.Status status) {
        Payment paymentFromDb = paymentRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException(
                        DIDNT_FIND_PAYMENT_WITH_SESSION_ID + sessionId));
        paymentFromDb.setStatus(status);
        Payment updatedPayment = paymentRepository.save(paymentFromDb);
        return paymentMapper.paymentToPaymentResponseDto(updatedPayment);
    }

    private Payment buildPayment(Session session, Long bookingId, BigDecimal amountToPay)
            throws MalformedURLException {
        Payment payment = new Payment();
        payment.setSessionUrl(new URL(session.getUrl()));
        payment.setSessionId(session.getId());
        payment.setBookingId(bookingId);
        payment.setAmountToPay(amountToPay);
        payment.setStatus(Payment.Status.PENDING);
        return payment;
    }
}
