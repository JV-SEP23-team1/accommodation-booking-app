package com.example.accommodationbookingapp.dto.accommodation;

import com.example.accommodationbookingapp.model.Accommodation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private List<String> amenitiesNames;
    @NotNull
    @Positive
    private BigDecimal dailyRate;
    @NotNull
    @Positive
    private Integer availability;
    private Accommodation.Type type;
}
