package com.example.accommodationbookingapp.repository.booking;

import com.example.accommodationbookingapp.model.Booking;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
