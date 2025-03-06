package com.ixtiyor.tgwebhookbotcore.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetMe;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TelegramBotManager {

    private final TelegramBotsApi telegramBotsApi;

    public TelegramBotManager(TelegramBotsApi telegramBotsApi) {
        this.telegramBotsApi = telegramBotsApi;
    }

    private final Map<String, TelegramWebhookBot> dynamicBots = new ConcurrentHashMap<>();


    /**
     * Register a new bot at runtime.
     */
    public void registerNewBot(String username, String token, String webhookPath, String host) {

        // 1) Create an anonymous TelegramWebhookBot (or use a normal class if you prefer)
        TelegramWebhookBot bot = new TelegramWebhookBot(token) {
//
//            @Override
//            public String getBotToken() {
//                return token;
//            }

            @Override
            public String getBotUsername() {
                return username;
            }

            @Override
            public String getBotPath() {
                // Each bot should have a unique path or prefix to distinguish it
                return webhookPath;
            }

            @Override
            public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
                // Implement logic for this bot
                // For example, respond to messages:
                System.out.println("Received update for bot " + username + ": " + update);
                // Return "OK" or something else for the HTTP response

                return null;
            }
        };

        // 2) Build the SetWebhook request with the public URL (i.e., https://mydomain.com/<webhookPath>)
        // This is how Telegram knows where to send the updates
        SetWebhook setWebhook = SetWebhook.builder()
                .url(host + "/" + webhookPath)
                .build();

        // 3) Register the bot with the TelegramBotsApi
        try {
            telegramBotsApi.registerBot(bot, setWebhook);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        // 4) Optionally store the bot instance if you want to remove/disable it later
        // dynamicBots.put(username, bot);
        dynamicBots.put(username, bot);

    }

    public void disableBot(String username, boolean dropPendingUpdates) {
        TelegramWebhookBot bot = dynamicBots.get(username);
        if (bot == null) {
            // handle "bot not found" scenario
            return;
        }

        // 1) Stop receiving updates by removing the webhook
        //    You can do it by sending a SetWebhook with an empty URL or by calling deleteWebhook
        //    For example:
        try {
            bot.execute(DeleteWebhook.builder().dropPendingUpdates(dropPendingUpdates).build());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        // 2) Optionally remove it from memory
        dynamicBots.remove(username);
    }

    public boolean isBotHealthy(String username) {
        TelegramWebhookBot bot = dynamicBots.get(username);
        if (bot == null) {
            return false;
        }
        try {
            // 'getMe()' is a quick way to check if the bot token is valid and the bot is accessible
            GetMe getMe = new GetMe();
            bot.execute(getMe);
            return true;
        } catch (TelegramApiException e) {
            return false;
        }
    }

    public List<String> getAllBots() {
        return new ArrayList<>(dynamicBots.keySet());
    }

    public void updateWebhookUrl(String username, String webhookPath, String host) throws TelegramApiException {
        TelegramWebhookBot bot = dynamicBots.get(username);
        if (bot == null) return;

        SetWebhook setWebhook = SetWebhook.builder()
                .url(host + webhookPath)
                .build();
        telegramBotsApi.registerBot(bot, setWebhook);
    }


}
