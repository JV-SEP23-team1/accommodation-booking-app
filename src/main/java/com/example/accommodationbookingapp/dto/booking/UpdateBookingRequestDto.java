package com.example.accommodationbookingapp.dto.booking;

import com.example.accommodationbookingapp.model.Accommodation;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UpdateBookingRequestDto {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Accommodation accommodationId;
}
