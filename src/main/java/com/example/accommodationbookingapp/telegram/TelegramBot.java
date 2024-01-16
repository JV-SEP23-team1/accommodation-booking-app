package com.example.accommodationbookingapp.telegram;

import com.example.accommodationbookingapp.model.Booking;
import com.example.accommodationbookingapp.model.User;
import com.example.accommodationbookingapp.repository.booking.BookingRepository;
import com.example.accommodationbookingapp.repository.user.UserRepository;
import com.vdurmont.emoji.EmojiParser;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@EnableScheduling
@Service
public class TelegramBot extends TelegramLongPollingBot {
    private static final String MSG_ERROR_TEXT = "Cannot execute message: ";
    private static final String AUTH_FAIL = "Authentication failed. "
            + "Please double-check your email and password and try again.";
    private static final String YES_BUTTON = "YES_BUTTON";
    private static final String NO_BUTTON = "NO_BUTTON";
    private static final String HELP_TEXT = """
            Welcome to the Accommodation-Booking-App Notification Bot!

            Explore various commands from the menu on the left or by typing a specific command:

            Type /start to see a welcome message

            Type /authenticate to begin the authentication process to start receiving notifications.
            
            Bot will request you to type email and password
            which you have used when register in Accommodation-Booking-App.
            
            NOTE: For security reasons, your credentials will be deleted after authentication!

            Type /cancel to stop receiving notifications from this bot.
            
            Type /help to see this message again""";
    private final List<Integer> messageIds = new ArrayList<>();
    private final Map<Long, AuthenticationContext> authenticationContextMap = new HashMap<>();
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BookingRepository bookingRepository;
    @Value("${admin.chatId}")
    private Long adminChatId;

    @Autowired
    public TelegramBot(@Value("${bot.token}") String botToken, UserRepository userRepository,
                       PasswordEncoder passwordEncoder, BookingRepository bookingRepository) {
        super(botToken);
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get a welcome message"));
        listOfCommands.add(new BotCommand("/help", "info how to use this bot"));
        listOfCommands.add(new BotCommand("/authenticate", "set your preferences"));
        listOfCommands.add(new BotCommand("/cancel", "set your preferences"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(),null));
        } catch (TelegramApiException e) {
            throw new RuntimeException("Error setting bot's command list: " + e.getMessage());
        }
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public String getBotUsername() {
        return "YourBotUsername";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (authenticationContextMap.containsKey(chatId)) {
                handleAuthentication(chatId, messageText, update);
            } else {
                switch (messageText) {
                    case "/start":
                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                        break;
                    case "/help":
                        sendMessage(chatId, HELP_TEXT);
                        break;
                    case "/cancel":
                        beforeCancel(chatId);
                        break;
                    case "/authenticate":
                        startAuthentication(chatId);
                        break;
                    default:
                        sendMessage(chatId, "Sorry, command was not recognized");
                }
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.equals(YES_BUTTON)) {
                cancelNotification(chatId);
                String text = "You are successfully cancel notification service";
                executeEditMessageText(text, chatId, messageId);
            } else if (callbackData.equals(NO_BUTTON)) {
                String text = "Thank you for staying with us ❤️";
                executeEditMessageText(text, chatId, messageId);
            }
        }
    }

    public void sendMessage(Long chatId,String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Cannot send a message" + e.getMessage());
        }
    }

    private void beforeCancel(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Do you really want to stop receiving notification?");

        var yesButton = new InlineKeyboardButton();

        yesButton.setText("Yes");
        yesButton.setCallbackData(YES_BUTTON);

        var noButton = new InlineKeyboardButton();

        noButton.setText("No");
        noButton.setCallbackData(NO_BUTTON);

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        rowInLine.add(yesButton);
        rowInLine.add(noButton);

        rowsInLine.add(rowInLine);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        executeMessage(message);
    }

    private void cancelNotification(Long chatId) {
        User user = userRepository.findByChatId(chatId).get();
        user.setChatId(null);
        userRepository.save(user);
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = EmojiParser.parseToUnicode("Hi, " + name + ", nice to meet you!"
                + " :blush:\n\n" + "Type /help to get info how to use this bot");
        sendMessage(chatId, answer);
    }

    private void startAuthentication(long chatId) {
        if (userRepository.existsUserByChatId(chatId)) {
            sendMessage(chatId, "You are already authenticated");
            return;
        }
        sendMessage(chatId, "Please enter your email to start the authentication process:");

        AuthenticationContext context = new AuthenticationContext();
        authenticationContextMap.put(chatId, context);
    }

    private void handleAuthentication(long chatId, String text, Update update) {
        Integer emailMessageId = update.getMessage().getMessageId();
        messageIds.add(emailMessageId);
        AuthenticationContext context = authenticationContextMap.get(chatId);
        if (context.getEmail() == null) {
            context.setEmail(text);
            sendMessage(chatId, "Email received. Now, please enter your password:");
        } else if (context.getPassword() == null) {
            Integer passwordMessageId = update.getMessage().getMessageId();
            messageIds.add(passwordMessageId);
            context.setPassword(text);

            if (userRepository.existsUserByEmail(context.email)) {
                User user = userRepository.findByEmail(context.email).get();
                String storedEncodedPassword = user.getPassword();

                if (passwordEncoder.matches(context.password, storedEncodedPassword)) {
                    user.setChatId(chatId);
                    userRepository.save(user);
                    sendMessage(chatId, "You have successfully authenticated");
                } else {
                    sendMessage(chatId, AUTH_FAIL);
                }
            } else {
                sendMessage(chatId, AUTH_FAIL);
            }
            delete(chatId);
            authenticationContextMap.remove(chatId);
        }
    }

    private void delete(Long chatId) {
        for (int i = 0;i < messageIds.size() - 1;i++) {
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(chatId.toString());
            deleteMessage.setMessageId(messageIds.get(i));

            try {
                execute(deleteMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(MSG_ERROR_TEXT + e.getMessage());
            }
        }
        messageIds.clear();
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(MSG_ERROR_TEXT + e.getMessage());
        }
    }

    private void executeEditMessageText(String text, long chatId, long messageId) {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(MSG_ERROR_TEXT + e.getMessage());
        }
    }

    @Scheduled(cron = "0 00 12 * * *")
    @Transactional
    public void sendAds() {
        int counterOfSendingMessageToUser = 0;
        List<Booking> allBookings = bookingRepository.findAll();
        for (var booking : allBookings) {
            if (booking.getCheckOutDate().isBefore(LocalDate.now().plusDays(2))
                    && booking.getStatus() != Booking.Status.CANCELED) {

                booking.setStatus(Booking.Status.EXPIRED);
                sendMessage(booking.getUser().getChatId(), formUserMessage(booking));
                sendMessage(adminChatId, formAdminMessage(booking));
                counterOfSendingMessageToUser++;
            }
        }
        if (counterOfSendingMessageToUser == 0) {
            sendMessage(adminChatId, "No expired bookings today!");
        }
    }

    private String formUserMessage(Booking booking) {
        return "Your booking by location: " + booking.getAccommodation().getLocation()
                + "will be expired on " + booking.getCheckOutDate();
    }

    private String formAdminMessage(Booking booking) {
        return booking.getUser().getFirstName() + " " + booking.getUser().getLastName()
                + " with email: " + booking.getUser().getEmail() + " booking "
                + booking.getAccommodation().getType()
                + " by location: " + booking.getAccommodation().getLocation()
                + " will be expired on " + booking.getCheckOutDate();
    }

    @Getter
    @Setter
    private static class AuthenticationContext {
        private String email;
        private String password;
    }
}
