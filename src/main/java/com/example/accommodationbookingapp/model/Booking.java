package com.example.accommodationbookingapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

@Entity
@Data
@SQLDelete(sql = "UPDATE bookings SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted=false")
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;
    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;
    @ManyToOne
    @JoinColumn(name = "accommodation_id", nullable = false)
    private Accommodation accommodation;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private Status status;
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    enum Status {
        PENDING,
        CONFIRMED,
        CANCELED,
        EXPIRED
    }
}
