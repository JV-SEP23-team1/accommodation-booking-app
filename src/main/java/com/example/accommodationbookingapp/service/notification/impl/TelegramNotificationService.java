package com.example.accommodationbookingapp.service.notification.impl;

import com.example.accommodationbookingapp.service.notification.NotificationService;
import com.example.accommodationbookingapp.telegram.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {
    private final TelegramBot telegramBot;

    @Override
    public void sendNotification(Long chatId, String message) {
        telegramBot.sendMessage(chatId, message);
    }
}
