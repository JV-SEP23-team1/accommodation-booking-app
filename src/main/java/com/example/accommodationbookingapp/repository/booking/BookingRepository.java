package com.example.accommodationbookingapp.repository.booking;

import com.example.accommodationbookingapp.model.Booking;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @EntityGraph(attributePaths = {"accommodation", "user"})
    List<Booking> findAllByUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"accommodation", "user"})
    List<Booking> findAllByUserIdAndStatus(Long userId, Booking.Status status, Pageable pageable);

    @EntityGraph(attributePaths = {"accommodation", "user"})
    List<Booking> findByStatus(Booking.Status status, Pageable pageable);

    @EntityGraph(attributePaths = {"accommodation", "user"})
    Optional<Booking> findByIdAndUserId(Long bookingId, Long userId);

    @EntityGraph(attributePaths = {"accommodation", "user"})
    void deleteByIdAndUserId(Long bookingId, Long userId);

    @Query("SELECT b FROM Booking b JOIN FETCH b.accommodation a LEFT JOIN FETCH a.amenities"
            + " WHERE b.user.id = :userId AND b.checkInDate BETWEEN :startDate AND :endDate")
    List<Booking> findAllByUserIdAndCheckInDateIsAfter(Long userId,
                                                       LocalDate startDate, LocalDate endDate);

    @Query("SELECT b FROM Booking b JOIN FETCH b.accommodation WHERE b.id = :bookingId")
    Optional<Booking> findBookingWithAccommodationById(@Param("bookingId") Long bookingId);

    @Query("SELECT count(*) > 0 FROM Booking b WHERE b.accommodation.id = :accommodationId AND ("
            + "            :checkInDate BETWEEN b.checkInDate AND b.checkOutDate"
            + "            OR :checkOutDate BETWEEN b.checkInDate AND b.checkOutDate"
            + "            OR b.checkInDate BETWEEN :checkInDate AND :checkOutDate"
            + "            OR b.checkOutDate BETWEEN :checkInDate AND :checkOutDate"
            + "    ) ")
    boolean existBookingAccommodationWithSameDate(
            Long accommodationId,
            LocalDate checkInDate,
            LocalDate checkOutDate);
}
