package com.example.accommodationbookingapp.service.notification.impl;

import com.example.accommodationbookingapp.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbookingapp.dto.booking.BookingResponseDto;
import com.example.accommodationbookingapp.model.User;
import com.example.accommodationbookingapp.repository.user.UserRepository;
import com.example.accommodationbookingapp.service.notification.NotificationService;
import com.example.accommodationbookingapp.telegram.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {
    @Value("${admin.chatId}")
    private Long adminChatId;
    private final TelegramBot telegramBot;
    private final UserRepository userRepository;

    @Override
    public void sendNotification(Long chatId, String message) {
        telegramBot.sendMessage(chatId, message);
    }

    @Override
    public void sendAccommodationCreateMessage(AccommodationResponseDto accommodationResponseDto) {
        String message = "New accommodation was created: " + System.lineSeparator()
                + "Type: " + accommodationResponseDto.getType() + System.lineSeparator()
                + "Location: " + accommodationResponseDto.getLocation() + System.lineSeparator()
                + "Size: " + accommodationResponseDto.getSize() + System.lineSeparator()
                + "Daily Rate: " + accommodationResponseDto.getDailyRate() + System.lineSeparator()
                + "Availability: " + accommodationResponseDto.getAvailability()
                + System.lineSeparator();

        sendNotification(adminChatId, message);
    }

    @Override
    public void sendAccommodationUpdateMessage(AccommodationResponseDto accommodationResponseDto) {
        String message = "Accommodation by id : " + accommodationResponseDto.getId()
                + " was updated" + System.lineSeparator()
                + "New Type: " + accommodationResponseDto.getType() + System.lineSeparator()
                + "New Location: " + accommodationResponseDto.getLocation() + System.lineSeparator()
                + "New Size: "
                + accommodationResponseDto.getSize() + System.lineSeparator()
                + "New Daily Rate: "
                + accommodationResponseDto.getDailyRate() + System.lineSeparator()
                + "New Availability: "
                + accommodationResponseDto.getAvailability() + System.lineSeparator();

        sendNotification(adminChatId, message);
    }

    @Override
    public void sendBookingCreateMessage(BookingResponseDto bookingResponseDto) {
        User user = userRepository.findById(bookingResponseDto.getUserId()).get();
        Long chatId = user.getChatId();
        String message = "You ordered a new booking for: " + bookingResponseDto.getCheckInDate()
                + "\nTo get more info about your bookings type /bookings";
        sendNotification(chatId, message);
    }

    @Override
    public void sendBookingUpdateMessage(BookingResponseDto bookingResponseDto) {
        User user = userRepository.findById(bookingResponseDto.getUserId()).get();
        Long chatId = user.getChatId();
        String message = "You updated an existing booking!"
                + "\nTo get new info about your bookings type /bookings";
        sendNotification(chatId, message);
    }
}
