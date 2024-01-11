package com.example.accommodationbookingapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

@Entity
@Data
@SQLDelete(sql = "UPDATE accomodations SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted=false")
@Table(name = "accomodations")
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private Type type;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private String size;
    @ElementCollection
    private List<String> amenities;
    @Column(name = "daily_rate", nullable = false)
    private BigDecimal dailyRate;
    @Column(nullable = false)
    private Integer availability;
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    enum Type {
        HOUSE,
        APARTMENT,
        CONDO,
        VACATION_HOME
    }
}
