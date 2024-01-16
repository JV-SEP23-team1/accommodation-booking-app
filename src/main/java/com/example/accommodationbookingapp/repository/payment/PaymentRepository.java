package com.example.accommodationbookingapp.repository.payment;

import com.example.accommodationbookingapp.model.Payment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("""
            SELECT p 
            FROM Payment p 
            JOIN FETCH Booking b 
            ON p.bookingId = b.id 
            WHERE b.user.id = :userId
            """)
    List<Payment> findAllByUserId(Long userId);

    Optional<Payment> findBySessionId(String sessionId);
}
