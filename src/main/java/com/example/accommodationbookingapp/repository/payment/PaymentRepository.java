package com.example.accommodationbookingapp.repository.payment;

import com.example.accommodationbookingapp.model.Payment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByBooking_User_Id(Long userId);
    Optional<Payment> findBySessionId(String sessionId);
}
