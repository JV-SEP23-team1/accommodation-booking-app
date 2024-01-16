package com.example.accommodationbookingapp.service.payment;

import com.example.accommodationbookingapp.dto.payment.PaymentResponseDto;
import com.example.accommodationbookingapp.model.Payment;

import java.net.MalformedURLException;
import java.util.List;

public interface PaymentService {

    List<PaymentResponseDto> findAllByUserId(Long userId);

    PaymentResponseDto create(Long bookingId) throws MalformedURLException;

    PaymentResponseDto update(String sessionId, Payment.Status status);
}

