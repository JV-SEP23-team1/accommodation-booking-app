package com.example.accommodationbookingapp.dto.booking;

import com.example.accommodationbookingapp.model.Accommodation;
import com.example.accommodationbookingapp.model.Booking;
import com.example.accommodationbookingapp.model.User;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateBookingRequestDto {
    @NotBlank
    private LocalDate checkInDate;
    @NotBlank
    private LocalDate checkOutDate;
    @NotBlank
    private Accommodation accommodationId;
    @NotBlank
    private User userId;
    private Booking.Status status;
}
