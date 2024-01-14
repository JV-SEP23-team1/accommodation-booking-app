package com.example.accommodationbookingapp.mapper;

import com.example.accommodationbookingapp.config.MapperConfig;
import com.example.accommodationbookingapp.dto.booking.BookingResponseDto;
import com.example.accommodationbookingapp.dto.booking.CreateBookingRequestDto;
import com.example.accommodationbookingapp.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface BookingMapper {
    Booking toModel(CreateBookingRequestDto requestDto);

    @Mapping(source = "accommodation.id", target = "accommodationId")
    @Mapping(source = "user.id", target = "userId")
    BookingResponseDto toResponseDto(Booking booking);
}
