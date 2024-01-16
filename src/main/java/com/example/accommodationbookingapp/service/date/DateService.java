package com.example.accommodationbookingapp.service.date;

import java.time.LocalDate;

public interface DateService {
    long calculateDurationInDays(LocalDate checkInDate, LocalDate checkOutDate);
}
