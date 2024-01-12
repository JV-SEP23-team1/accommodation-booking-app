package com.example.accommodationbookingapp.mapper;

import com.example.accommodationbookingapp.config.MapperConfig;
import com.example.accommodationbookingapp.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbookingapp.model.Accommodation;
import com.example.accommodationbookingapp.model.Amenity;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface AccommodationMapper {
    @Mapping(target = "amenities", ignore = true)
    AccommodationResponseDto toResponseDto(Accommodation accommodation);

    @AfterMapping
    default void setAmenities(@MappingTarget AccommodationResponseDto responseDto,
                              Accommodation accommodation) {
        if (accommodation.getAmenities() != null) {
            responseDto.setAmenities(accommodation.getAmenities().stream()
                    .map(Amenity::getName)
                    .collect(Collectors.toList()));
        }
    }
}

