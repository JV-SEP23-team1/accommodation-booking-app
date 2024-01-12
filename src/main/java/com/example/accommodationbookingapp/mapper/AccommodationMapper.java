package com.example.accommodationbookingapp.mapper;

import com.example.accommodationbookingapp.config.MapperConfig;
import com.example.accommodationbookingapp.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbookingapp.dto.accommodation.CreateAccommodationRequestDto;
import com.example.accommodationbookingapp.model.Accommodation;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface AccommodationMapper {
    AccommodationResponseDto toResponseDto(Accommodation accommodation);

    Accommodation toModel(CreateAccommodationRequestDto requestDto);
}

