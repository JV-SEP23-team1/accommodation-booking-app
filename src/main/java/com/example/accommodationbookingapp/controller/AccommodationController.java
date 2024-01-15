package com.example.accommodationbookingapp.controller;

import com.example.accommodationbookingapp.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbookingapp.dto.accommodation.CreateAccommodationRequestDto;
import com.example.accommodationbookingapp.dto.accommodation.UpdateAccommodationRequestDto;
import com.example.accommodationbookingapp.service.accommodation.AccommodationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Accommodations manager", description = "Endpoint for managing accommodations")
@RestController
@RequiredArgsConstructor
@RequestMapping("/accommodations")
public class AccommodationController {
    private final AccommodationService accommodationService;

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AccommodationResponseDto save(
            @RequestBody @Valid CreateAccommodationRequestDto requestDto
    ) {
        return accommodationService.save(requestDto);
    }

    @GetMapping
    @Operation(summary = "Get all accommodations as list",
            description = "Get a list of all the accommodations as dtos")
    public List<AccommodationResponseDto> getAll(Pageable pageable) {
        return accommodationService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get accommodation by ID",
            description = "Get accommodation as dto by its id")
    public AccommodationResponseDto getById(@PathVariable Long id) {
        return accommodationService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update accommodation by id",
            description = "Update accommodation fields (return updated accommodation as dto)")
    public AccommodationResponseDto updateById(
            @PathVariable Long id, @RequestBody UpdateAccommodationRequestDto requestDto
    ) {
        return accommodationService.updateById(id, requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete accommodation by ID",
            description = "Delete accommodation by id")
    public void deleteById(@PathVariable Long id) {
        accommodationService.deleteById(id);
    }
}
