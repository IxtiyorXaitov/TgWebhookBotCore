package com.ixtiyor.tgwebhookbotcore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultWebhook;

@Configuration
public class TelegramConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        // If using webhook mode:
        DefaultWebhook defaultWebhook = new DefaultWebhook();
        // e.g., for local dev or behind a proxy
        defaultWebhook.setInternalUrl("http://localhost:8080/webhook");

        return new TelegramBotsApi(DefaultBotSession.class, defaultWebhook);
    }
}