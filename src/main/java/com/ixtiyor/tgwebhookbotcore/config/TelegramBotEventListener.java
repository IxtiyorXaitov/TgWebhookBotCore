package com.ixtiyor.tgwebhookbotcore.config;

import com.ixtiyor.tgwebhookbotcore.service.TelegramBotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBotEventListener {

    private final TelegramBotService telegramBotService;

    @EventListener(ApplicationReadyEvent.class)
    public void startBot() {
        telegramBotService.registerAllBot();
        log.info("Telegram Bots are UP and Ready!");
    }

    @EventListener(ContextClosedEvent.class)
    public void stopBot() {
        telegramBotService.unregisterAllBot();
        log.info("Telegram Bots are DOWN!");
    }
}
