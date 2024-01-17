package com.example.accommodationbookingapp.controller;

import com.example.accommodationbookingapp.dto.payment.CreatePaymentDto;
import com.example.accommodationbookingapp.dto.payment.PaymentResponseDto;
import com.example.accommodationbookingapp.model.Payment;
import com.example.accommodationbookingapp.model.User;
import com.example.accommodationbookingapp.service.payment.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payment management", description = "Endpoints for managing payments")
public class PaymentController {
    private static final String SUCCESSFUL_PAYMENT = "Paid successfully for the Session: ";
    private static final String CANCELED_PAYMENT = "Payment canceled for the Session: ";
    private final PaymentService paymentService;
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Get Payments by User ID",
            description = "Get Payments as dto by User ID")
    public List<PaymentResponseDto> getPaymentsByUserId(@PathVariable Long id) {
        return paymentService.findAllByUserId(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping()
    @Operation(summary = "Get Payments for certain User",
            description = "Get Payments as dto by User ID")
    public List<PaymentResponseDto> getPaymentsForUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return paymentService.findAllByUserId(user.getId());
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Create new Payment",
            description = "Create new Payment using CreatePaymentDto")
    public PaymentResponseDto initiatePaymentSession(@RequestBody CreatePaymentDto requestDto, HttpServletResponse response)
            throws IOException {
        return paymentService.create(requestDto.getBookingId());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/success")
    @Operation(summary = "Updated Payment status in case of successful payment",
            description = "Updated Payment status to PAID in case of successful payment")
    public String handlePaymentSuccess(@RequestParam("session_id") String sessionId) {
        paymentService.update(sessionId, Payment.Status.PAID);
        return SUCCESSFUL_PAYMENT + sessionId;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/cancel")
    @Operation(summary = "Updated Payment status in case of canceled payment",
            description = "Updated Payment status to CANCELED in case of canceled payment")
    public String handlePaymentCancel(@RequestParam("session_id") String sessionId) {
        paymentService.update(sessionId, Payment.Status.CANCELED);
        return CANCELED_PAYMENT + sessionId;
    }
}
