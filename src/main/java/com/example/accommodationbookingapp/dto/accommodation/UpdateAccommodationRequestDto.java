package com.example.accommodationbookingapp.dto.accommodation;

import com.example.accommodationbookingapp.model.Accommodation;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class UpdateAccommodationRequestDto {
    private Accommodation.Type type;
    private String location;
    private String size;
    private List<Long> amenitiesIds;
    @Positive
    private BigDecimal dailyRate;
    @Positive
    private Integer availability;
}
