package com.example.accommodationbookingapp.repository.booking;

import com.example.accommodationbookingapp.model.Booking;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b JOIN FETCH b.accommodation a LEFT JOIN FETCH a.amenities"
            + " WHERE b.user.id = :userId AND b.checkInDate BETWEEN :startDate AND :endDate")
    List<Booking> findAllByUserIdAndCheckInDateIsAfter(Long userId,
                                                       LocalDate startDate, LocalDate endDate);
}
