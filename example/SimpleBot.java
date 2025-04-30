package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

public class SimpleBot extends TelegramLongPollingBot {

    private final Map<Long, String> userData = new HashMap<>();
    private final Map<Long, Integer> userStep = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        Message message = update.getMessage();
        long chatId = message.getChatId();
        String userInput = message.getText().trim();

        if (userInput.equalsIgnoreCase("/start")) {
            userStep.put(chatId, 1);
            userData.put(chatId, "");
            sendMessage(chatId, "Welcome! Please enter your birthdate (format: dd.MM.yyyy):");
            return;
        }

        int step = userStep.getOrDefault(chatId, 0);

        switch (step) {
            case 1:
                userData.put(chatId, "Birthdate: " + userInput + "\n");
                userStep.put(chatId, 2);
                sendMessage(chatId, "Great! What's your favorite color?");
                break;
            case 2:
                userData.put(chatId, userData.get(chatId) + "Color: " + userInput + "\n");
                userStep.put(chatId, 3);
                sendMessage(chatId, "What's your secret word?");
                break;
            case 3:
                userData.put(chatId, userData.get(chatId) + "Secret Word: " + userInput + "\n");
                userStep.put(chatId, 4);
                sendMessage(chatId, "Where are you from? (City)");
                break;
            case 4:
                userData.put(chatId, userData.get(chatId) + "City: " + userInput + "\n");
                userStep.put(chatId, 0);
                sendMessage(chatId, "Thank you! Here's the information you provided:\n" + userData.get(chatId));
                sendMessage(chatId, "Type /start to provide your details again.");
                break;
            default:
                sendMessage(chatId, "To begin, type /start.");
                break;
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "https://t.me/Bot_madeBy_stepBy_step_bot";
    }

    @Override
    public String getBotToken() {
        return "7731161490:AAGW99_ZM93bgRLDQHsgoEl5NS2eNLdvl_M";
    }
}

