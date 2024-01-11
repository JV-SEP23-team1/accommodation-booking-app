package com.example.accommodationbookingapp.dto.booking;

import com.example.accommodationbookingapp.model.Accommodation;
import com.example.accommodationbookingapp.model.Booking;
import com.example.accommodationbookingapp.model.User;
import java.time.LocalDate;
import lombok.Data;

@Data
public class BookingResponseDto {
    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long accommodationId;
    private Long userId;
    private Booking.Status status;
}
