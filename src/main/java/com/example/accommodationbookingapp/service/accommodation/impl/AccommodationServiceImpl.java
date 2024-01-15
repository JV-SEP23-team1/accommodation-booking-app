package com.example.accommodationbookingapp.service.accommodation.impl;

import com.example.accommodationbookingapp.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbookingapp.dto.accommodation.CreateAccommodationRequestDto;
import com.example.accommodationbookingapp.dto.accommodation.UpdateAccommodationRequestDto;
import com.example.accommodationbookingapp.exception.EntityNotFoundException;
import com.example.accommodationbookingapp.mapper.AccommodationMapper;
import com.example.accommodationbookingapp.model.Accommodation;
import com.example.accommodationbookingapp.model.Amenity;
import com.example.accommodationbookingapp.repository.accommodation.AccommodationRepository;
import com.example.accommodationbookingapp.service.accommodation.AccommodationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {
    private static final String CANNOT_FIND_ACCOMMODATION_BY_ID_MSG
            = "Cannot find accommodation by id: ";
    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;

    @Override
    public AccommodationResponseDto save(CreateAccommodationRequestDto requestDto) {
        Accommodation accommodationModel = accommodationMapper.toModel(requestDto);
        accommodationRepository.save(accommodationModel);
        return accommodationMapper.toResponseDto(accommodationModel);
    }

    @Override
    public List<AccommodationResponseDto> getAll(Pageable pageable) {
        return accommodationRepository.findAll(pageable).stream()
                .map(accommodationMapper::toResponseDto)
                .toList();
    }

    @Override
    public AccommodationResponseDto findById(Long id) {
        Accommodation accommodationFromDb = accommodationRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                CANNOT_FIND_ACCOMMODATION_BY_ID_MSG + id));
        return accommodationMapper.toResponseDto(accommodationFromDb);
    }

    @Override
    public AccommodationResponseDto updateById(
            Long id, UpdateAccommodationRequestDto updateRequestDto
    ) {
        Accommodation accommodationFromDb = accommodationRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                CANNOT_FIND_ACCOMMODATION_BY_ID_MSG + id));
        setUpdatedFields(updateRequestDto, accommodationFromDb);
        accommodationRepository.save(accommodationFromDb);
        return accommodationMapper.toResponseDto(accommodationFromDb);
    }

    @Override
    public void deleteById(Long id) {
        accommodationRepository.deleteById(id);
    }

    private void setUpdatedFields(
            UpdateAccommodationRequestDto updateRequestDto, Accommodation accommodation
    ) {
        if (updateRequestDto.getAmenitiesIds() != null) {
            List<Amenity> amenities = updateRequestDto.getAmenitiesIds()
                    .stream()
                    .map(Amenity::new)
                    .toList();
            accommodation.setAmenities(amenities);
        }
        if (updateRequestDto.getType() != null) {
            accommodation.setType(updateRequestDto.getType());
        }
        if (updateRequestDto.getSize() != null) {
            accommodation.setSize(updateRequestDto.getSize());
        }
        if (updateRequestDto.getLocation() != null) {
            accommodation.setLocation(updateRequestDto.getLocation());
        }
        if (updateRequestDto.getAvailability() != null) {
            accommodation.setAvailability(updateRequestDto.getAvailability());
        }
        if (updateRequestDto.getDailyRate() != null) {
            accommodation.setDailyRate(updateRequestDto.getDailyRate());
        }
    }
}
