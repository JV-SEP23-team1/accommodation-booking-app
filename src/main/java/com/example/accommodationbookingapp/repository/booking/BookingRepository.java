package com.example.accommodationbookingapp.repository.booking;

import com.example.accommodationbookingapp.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
