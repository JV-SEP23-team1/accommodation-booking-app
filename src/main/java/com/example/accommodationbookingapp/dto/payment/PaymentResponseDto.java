package com.example.accommodationbookingapp.dto.payment;

import com.example.accommodationbookingapp.model.Payment;
import java.math.BigDecimal;
import java.net.URL;
import lombok.Data;

@Data
public class PaymentResponseDto {
    private Long id;
    private Payment.Status status;
    private URL sessionUrl;
    private String sessionId;
    private Long bookingId;
    private BigDecimal amountToPay;
}
