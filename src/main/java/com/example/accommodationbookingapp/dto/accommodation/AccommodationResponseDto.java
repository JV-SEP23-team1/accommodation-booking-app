package com.example.accommodationbookingapp.dto.accommodation;

import com.example.accommodationbookingapp.model.Accommodation;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class AccommodationResponseDto {
    private Long id;

    private Accommodation.Type type;

    private String location;

    private String size;

    private List<Long> amenitiesIds;

    private BigDecimal dailyRate;

    private Integer availability;
}
