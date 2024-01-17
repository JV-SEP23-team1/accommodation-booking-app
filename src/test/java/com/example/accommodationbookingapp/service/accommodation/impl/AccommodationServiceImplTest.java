package com.example.accommodationbookingapp.service.accommodation.impl;

import static org.mockito.Mockito.when;

import com.example.accommodationbookingapp.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbookingapp.dto.accommodation.CreateAccommodationRequestDto;
import com.example.accommodationbookingapp.mapper.AccommodationMapper;
import com.example.accommodationbookingapp.model.Accommodation;
import com.example.accommodationbookingapp.repository.accommodation.AccommodationRepository;
import com.example.accommodationbookingapp.service.notification.NotificationService;
import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccommodationServiceImplTest {
    private static CreateAccommodationRequestDto createAccommodationRequestDto;
    private static AccommodationResponseDto accommodationResponseDto;
    private static Accommodation accommodationModel;

    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private AccommodationMapper accommodationMapper;

    @InjectMocks
    private AccommodationServiceImpl accommodationServiceImpl;

    @BeforeAll
    static void beforeAll() {
        createAccommodationRequestDto = new CreateAccommodationRequestDto();
        createAccommodationRequestDto.setLocation("Kyiv");
        createAccommodationRequestDto.setSize("Medium");
        createAccommodationRequestDto.setAmenitiesIds(List.of(1L, 2L));
        createAccommodationRequestDto.setDailyRate(BigDecimal.valueOf(4000.0));
        createAccommodationRequestDto.setAvailability(4);
        createAccommodationRequestDto.setType(Accommodation.Type.APARTMENT);

        accommodationResponseDto = new AccommodationResponseDto();
        accommodationResponseDto.setId(1L);
        accommodationResponseDto.setLocation(createAccommodationRequestDto.getLocation());
        accommodationResponseDto.setSize(createAccommodationRequestDto.getSize());
        accommodationResponseDto.setAmenitiesIds(createAccommodationRequestDto.getAmenitiesIds());
        accommodationResponseDto.setDailyRate(createAccommodationRequestDto.getDailyRate());
        accommodationResponseDto.setAvailability(createAccommodationRequestDto.getAvailability());
        accommodationResponseDto.setType(createAccommodationRequestDto.getType());

        accommodationModel = new Accommodation();
        accommodationModel.setId(1L);
        accommodationModel.setLocation(createAccommodationRequestDto.getLocation());
        accommodationModel.setSize(createAccommodationRequestDto.getSize());
        accommodationModel.setDailyRate(createAccommodationRequestDto.getDailyRate());
        accommodationModel.setAvailability(createAccommodationRequestDto.getAvailability());
        accommodationModel.setType(createAccommodationRequestDto.getType());
    }

    @Test
    public void save_ValidAccommodation_Successful() {

        when(accommodationMapper.toModel(createAccommodationRequestDto))
                .thenReturn(accommodationModel);
        when(accommodationRepository.save(accommodationModel))
                .thenReturn(accommodationModel);
        when(accommodationMapper.toResponseDto(accommodationModel))
                .thenReturn(accommodationResponseDto);

        AccommodationResponseDto expectedDto = accommodationResponseDto;

        AccommodationResponseDto actualDto = accommodationServiceImpl
                .save(createAccommodationRequestDto);

        Assertions.assertNotNull(actualDto);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expectedDto, actualDto, "id"));
    }
}
