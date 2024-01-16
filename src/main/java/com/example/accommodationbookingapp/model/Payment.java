package com.example.accommodationbookingapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.net.URL;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Data
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private Status status;
    @Column(name = "session_url", nullable = false)
    private URL sessionUrl;
    @Column(name = "session_id", nullable = false)
    private String sessionId;
    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    @Column(name = "amount_to_pay", nullable = false)
    private BigDecimal amountToPay;

    public enum Status {
        PENDING,
        PAID,
        CANCELED
    }
}
