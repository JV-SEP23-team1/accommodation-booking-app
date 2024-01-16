package com.example.accommodationbookingapp.service.date.impl;

import com.example.accommodationbookingapp.service.date.DateService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;

@Service
public class DateServiceImpl implements DateService {
    public static final int GAP = 1;

    @Override
    public long calculateDurationInDays(LocalDate checkInDate, LocalDate checkOutDate) {
        Duration duration = Duration.between(
                checkInDate.atStartOfDay(), checkOutDate.atStartOfDay());
        return duration.toDays() + GAP;
    }
}
