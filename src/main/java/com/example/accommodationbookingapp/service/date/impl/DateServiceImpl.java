package com.example.accommodationbookingapp.service.date.impl;

import com.example.accommodationbookingapp.service.date.DateService;
import java.time.Duration;
import java.time.LocalDate;

public class DateServiceImpl implements DateService {
    public static final int GAP = 1;

    @Override
    public long calculateDurationInDays(LocalDate checkInDate, LocalDate checkOutDate) {
        Duration duration = Duration.between(
                checkInDate.atStartOfDay(), checkOutDate.atStartOfDay());
        return duration.toDays() + GAP;
    }
}
