package com.example.accommodationbookingapp.mapper;

import com.example.accommodationbookingapp.config.MapperConfig;
import com.example.accommodationbookingapp.dto.payment.PaymentResponseDto;
import com.example.accommodationbookingapp.model.Payment;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    PaymentResponseDto paymentToPaymentResponseDto(Payment payment);

    List<PaymentResponseDto> paymentsToPaymentResponseDtos(List<Payment> payments);

}
