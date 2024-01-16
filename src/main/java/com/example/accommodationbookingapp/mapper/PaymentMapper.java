package com.example.accommodationbookingapp.mapper;

import com.example.accommodationbookingapp.config.MapperConfig;
import com.example.accommodationbookingapp.dto.payment.PaymentResponseDto;
import com.example.accommodationbookingapp.model.Payment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    PaymentResponseDto paymentToPaymentResponseDto(Payment payment);

    List<PaymentResponseDto> paymentsToPaymentResponseDtos(List<Payment> payments);

}
