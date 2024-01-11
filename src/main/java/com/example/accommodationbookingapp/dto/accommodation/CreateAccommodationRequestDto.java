package com.example.accommodationbookingapp.dto.accommodation;

import com.example.accommodationbookingapp.model.Accommodation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class CreateAccommodationRequestDto {
    @NotBlank
    private String location;
    @NotBlank
    private String size;
    @NotBlank
    private List<String> amenities;
    @NotBlank
    @Positive
    private BigDecimal dailyRate;
    @NotBlank
    @Positive
    private Integer availability;
    private Accommodation.Type type;
}
