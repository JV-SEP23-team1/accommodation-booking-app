package com.example.accommodationbookingapp.service.notification;

import com.example.accommodationbookingapp.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbookingapp.dto.booking.BookingResponseDto;
import com.example.accommodationbookingapp.dto.payment.PaymentResponseDto;

public interface NotificationService {
    void sendNotification(Long chatId, String message);

    void sendAccommodationCreateMessage(AccommodationResponseDto accommodationResponseDto);

    void sendAccommodationUpdateMessage(AccommodationResponseDto accommodationResponseDto);

    void sendBookingCreateMessage(BookingResponseDto bookingResponseDto);

    void sendBookingUpdateMessage(BookingResponseDto bookingResponseDto);

    void sendPaymentMessage(PaymentResponseDto responseDto);
}
