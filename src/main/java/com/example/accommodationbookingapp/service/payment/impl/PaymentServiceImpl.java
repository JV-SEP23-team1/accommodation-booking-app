package com.example.accommodationbookingapp.service.payment.impl;

import com.example.accommodationbookingapp.dto.payment.PaymentResponseDto;
import com.example.accommodationbookingapp.mapper.PaymentMapper;
import com.example.accommodationbookingapp.model.Booking;
import com.example.accommodationbookingapp.model.Payment;
import com.example.accommodationbookingapp.repository.payment.PaymentRepository;
import com.example.accommodationbookingapp.service.payment.PaymentService;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.example.accommodationbookingapp.service.session.SessionService;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    public static final String DIDNT_FIND_PAYMENT_WITH_SESSION_ID =
            "Didn't find Payment with SessionId:";
    private PaymentRepository paymentRepository;
    private PaymentMapper paymentMapper;
    private SessionService sessionService;


    @Override
    public List<PaymentResponseDto> findAllByUserId(Long userId) {
        List<Payment> paymentsFromDb = paymentRepository.findAllByBooking_User_Id(userId);
        return paymentMapper.paymentsToPaymentResponseDtos(paymentsFromDb);
    }

    @Override
    public PaymentResponseDto create(Long bookingId) throws MalformedURLException {

        Booking booking = new Booking();

        Session session = sessionService.createPaymentSession(new BigDecimal("100.00"));

        Payment createdPayment = buildPayment(session, booking);
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

    private Payment buildPayment(Session session, Booking booking) throws MalformedURLException {
        Payment payment = new Payment();
        payment.setSessionUrl(new URL(session.getUrl()));
        payment.setSessionId(session.getId());
        payment.setBooking(booking);
        payment.setAmountToPay(new BigDecimal("100.00"));
        payment.setStatus(Payment.Status.PENDING);
        return payment;
    }

}
