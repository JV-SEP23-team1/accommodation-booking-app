package com.example.accommodationbookingapp.controller;

import com.example.accommodationbookingapp.dto.booking.BookingResponseDto;
import com.example.accommodationbookingapp.dto.booking.CreateBookingRequestDto;
import com.example.accommodationbookingapp.dto.booking.UpdateBookingRequestDto;
import com.example.accommodationbookingapp.model.Booking;
import com.example.accommodationbookingapp.model.User;
import com.example.accommodationbookingapp.service.booking.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
@Tag(name = "Booking management", description = "Endpoints for managing bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Create a new booking")
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto createBooking(
            @RequestBody @Valid CreateBookingRequestDto requestDto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return bookingService.createBooking(user.getId(), requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(
            summary = "Retrieves bookings",
            description = "Retrieves bookings based on user ID and their status"
    )
    public List<BookingResponseDto> getBookingsByUserIdAndStatus(
            @RequestParam(name = "user_id") Optional<Long> optionalId,
            @RequestParam(name = "status") Booking.Status status,
            Pageable pageable
    ) {
        return bookingService.getBookings(optionalId, status, pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    @Operation(
            summary = "Retrieves user bookings",
            description = "Retrieves bookings for the authenticated user")
    public List<BookingResponseDto> getUserBookings(
            Authentication authentication,
            Pageable pageable
    ) {
        User user = (User) authentication.getPrincipal();
        return bookingService.getBookingHistory(user.getId(), pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    @Operation(
            summary = "Get the booking",
            description = "Provides information about a specific booking"
    )
    public BookingResponseDto getBooking(
            Authentication authentication,
            @PathVariable(name = "id") Long bookingId) {
        User user = (User) authentication.getPrincipal();
        return bookingService.getBookingByIdAndUserId(bookingId, user.getId());
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{id}")
    @Operation(
            summary = "Update the booking",
            description = "Allows users to update their booking details")
    public BookingResponseDto updateBooking(
            Authentication authentication, @PathVariable(name = "id") Long bookingId,
            @RequestBody @Valid UpdateBookingRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return bookingService.updateBookingDetails(user.getId(), bookingId, requestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Delete the booking",
            description = "Enables the cancellation of bookings"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooking(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        bookingService.deleteByBookingIdAndUserId(id, user.getId());
    }
}
