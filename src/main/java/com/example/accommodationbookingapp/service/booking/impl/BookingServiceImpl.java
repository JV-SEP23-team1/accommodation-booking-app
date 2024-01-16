package com.example.accommodationbookingapp.service.booking.impl;

import com.example.accommodationbookingapp.dto.booking.BookingResponseDto;
import com.example.accommodationbookingapp.dto.booking.CreateBookingRequestDto;
import com.example.accommodationbookingapp.dto.booking.UpdateBookingRequestDto;
import com.example.accommodationbookingapp.exception.EntityNotFoundException;
import com.example.accommodationbookingapp.mapper.BookingMapper;
import com.example.accommodationbookingapp.model.Accommodation;
import com.example.accommodationbookingapp.model.Booking;
import com.example.accommodationbookingapp.model.User;
import com.example.accommodationbookingapp.repository.accommodation.AccommodationRepository;
import com.example.accommodationbookingapp.repository.booking.BookingRepository;
import com.example.accommodationbookingapp.repository.user.UserRepository;
import com.example.accommodationbookingapp.service.booking.BookingService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private static final String BOOKING_NOT_FOUND_EXCEPTION =
            "Can't find the booking with ID: ";
    private static final String USER_NOT_FOUND_EXCEPTION =
            "Can't find the user with ID: ";
    private static final String ACCOMMODATION_NOT_FOUND_EXCEPTION =
            "Can't find the accommodation by ID: ";

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final AccommodationRepository accommodationRepository;

    @Transactional
    @Override
    public BookingResponseDto createBooking(Long userId, CreateBookingRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        USER_NOT_FOUND_EXCEPTION + userId
                ));
        Accommodation accommodation = getAccommodationById(requestDto.getAccommodationId());
        Booking booking = new Booking();
        booking.setCheckInDate(requestDto.getCheckInDate());
        booking.setCheckOutDate(requestDto.getCheckOutDate());
        booking.setAccommodation(accommodation);
        booking.setUser(user);
        booking.setStatus(Booking.Status.CONFIRMED);
        return bookingMapper.toResponseDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getBookings(
            Optional<Long> optionalId,
            Booking.Status status,
            Pageable pageable
    ) {
        return optionalId.map(id -> getBookingsByUserIdAndStatus(id, status, pageable))
                .orElseGet(() -> getBookingsByStatus(status, pageable));
    }

    private List<BookingResponseDto> getBookingsByUserIdAndStatus(
            Long userId,
            Booking.Status status,
            Pageable pageable
    ) {
        return bookingRepository.findAllByUserIdAndStatus(userId, status, pageable)
                .stream()
                .map(bookingMapper::toResponseDto)
                .toList();
    }

    private List<BookingResponseDto> getBookingsByStatus(
            Booking.Status status,
            Pageable pageable
    ) {
        return bookingRepository.findByStatus(status, pageable)
                .stream()
                .map(bookingMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getBookingHistory(Long userId, Pageable pageable) {
        return bookingRepository.findAllByUserId(userId, pageable)
                .stream()
                .map(bookingMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public BookingResponseDto getBookingByIdAndUserId(Long bookingId, Long userId) {
        Booking booking = getByBookingIdAndUserId(bookingId, userId);
        return bookingMapper.toResponseDto(booking);
    }

    @Transactional
    @Override
    public BookingResponseDto updateBookingDetails(
            Long userId,
            Long bookingId,
            UpdateBookingRequestDto requestDto
    ) {
        Booking booking = getByBookingIdAndUserId(bookingId, userId);
        Accommodation accommodation = getAccommodationById(requestDto.getAccommodationId());
        booking.setCheckInDate(requestDto.getCheckInDate());
        booking.setCheckOutDate(requestDto.getCheckOutDate());
        booking.setAccommodation(accommodation);
        return bookingMapper.toResponseDto(bookingRepository.save(booking));
    }

    @Override
    public void deleteByBookingIdAndUserId(Long bookingId, Long userId) {
        bookingRepository.deleteByIdAndUserId(bookingId, userId);
    }

    private Booking getByBookingIdAndUserId(Long bookingId, Long userId) {
        return bookingRepository.findByIdAndUserId(bookingId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        BOOKING_NOT_FOUND_EXCEPTION + bookingId
                ));
    }

    private Accommodation getAccommodationById(Long id) {
        return accommodationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        ACCOMMODATION_NOT_FOUND_EXCEPTION + id
                ));
    }
}
