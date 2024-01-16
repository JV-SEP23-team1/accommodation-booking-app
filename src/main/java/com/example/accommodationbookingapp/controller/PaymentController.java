package com.example.accommodationbookingapp.controller;

import com.example.accommodationbookingapp.dto.payment.CreatePaymentDto;
import com.example.accommodationbookingapp.dto.payment.PaymentResponseDto;
import com.example.accommodationbookingapp.model.Payment;
import com.example.accommodationbookingapp.service.payment.PaymentService;
import com.example.accommodationbookingapp.service.url.UriService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private PaymentService paymentService;
    private UriService uriService;

    @GetMapping("/{id}")
    @Operation(summary = "Get Payments by User ID",
            description = "Get Payments as dto by User ID")
    public List<PaymentResponseDto> getPaymentsByUserId(@PathVariable Long id) {
        return paymentService.findAllByUserId(id);
    }

    @PostMapping
    @Operation(summary = "Create new  Payment",
            description = "Create new  Payment using CreatePaymentDto")
    public PaymentResponseDto initiatePaymentSession(@RequestBody CreatePaymentDto requestDto,
                                                     HttpServletResponse response)
            throws IOException {
        PaymentResponseDto createdPaymentDto = paymentService.create(requestDto.getBookingId());
        URI redirectUrl = uriService.buildUriWithSessionId(
                createdPaymentDto.getSessionId(),
                createdPaymentDto.getSessionUrl());
        response.sendRedirect(String.valueOf(redirectUrl));
        return createdPaymentDto;

    }

    @GetMapping("/success")
    @Operation(summary = "Updated Payment status in case of successful payment",
            description = "Updated Payment status to PAID in case of successful payment")
    public PaymentResponseDto handlePaymentSuccess(@RequestParam String sessionId) {
        return paymentService.update(sessionId, Payment.Status.PAID);
    }

    @GetMapping("/cancel")
    @Operation(summary = "Updated Payment status in case of canceled payment",
            description = "Updated Payment status to CANCELED in case of canceled payment")
    public PaymentResponseDto handlePaymentCancel(@RequestParam String sessionId) {


        return paymentService.update(sessionId, Payment.Status.CANCELED);
    }
}
