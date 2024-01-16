package com.example.accommodationbookingapp.service.accommodation;

import com.example.accommodationbookingapp.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbookingapp.dto.accommodation.CreateAccommodationRequestDto;
import com.example.accommodationbookingapp.dto.accommodation.UpdateAccommodationRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface AccommodationService {
    AccommodationResponseDto save(CreateAccommodationRequestDto requestDto);

    List<AccommodationResponseDto> getAll(Pageable pageable);

    AccommodationResponseDto findById(Long id);

    AccommodationResponseDto updateById(Long id, UpdateAccommodationRequestDto updateRequestDto);

    void deleteById(Long id);
}
