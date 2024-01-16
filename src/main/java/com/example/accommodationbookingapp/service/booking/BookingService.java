package com.example.accommodationbookingapp.service.booking;

import com.example.accommodationbookingapp.dto.booking.BookingResponseDto;
import com.example.accommodationbookingapp.dto.booking.CreateBookingRequestDto;
import com.example.accommodationbookingapp.dto.booking.UpdateBookingRequestDto;
import com.example.accommodationbookingapp.model.Booking;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookingService {
    BookingResponseDto createBooking(Long userId, CreateBookingRequestDto requestDto);

    List<BookingResponseDto> getBookingsByUserIdAndStatus(
            Long userId,
            Booking.Status status,
            Pageable pageable
    );

    List<BookingResponseDto> getBookingsByStatus(
            Booking.Status status,
            Pageable pageable
    );

    List<BookingResponseDto> getBookingHistory(Long userId, Pageable pageable);

    BookingResponseDto getBookingByIdAndUserId(Long bookingId, Long userId);

    BookingResponseDto updateBookingDetails(
            Long userId,
            Long bookingId,
            UpdateBookingRequestDto requestDto
    );

    void deleteById(Long id);
}
