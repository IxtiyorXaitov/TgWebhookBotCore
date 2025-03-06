package com.ixtiyor.tgwebhookbotcore.service;

import com.ixtiyor.tgwebhookbotcore.dto.telegram.TelegramBotDTO;
import com.ixtiyor.tgwebhookbotcore.dto.telegram.TelegramStatusDTO;
import com.ixtiyor.tgwebhookbotcore.repository.TelegramBotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBotService {
    private final TelegramBotManager telegramBotManager;
    private final TelegramBotRepository repository;

    public TelegramBotDTO create(TelegramBotDTO dto) {
        if (checkExistsWithUsername(dto.getUsername())) {
            throw new RuntimeException("telegram bot already exits username");
        }
        TelegramBotDTO entity = new TelegramBotDTO(
                dto.getUsername(),
                dto.getToken(),
                dto.getWebhookPath(),
                dto.getHost()
        );
        repository.save(entity);
        return entity;
    }

    public TelegramBotDTO update(UUID id, TelegramBotDTO dto) {
        if (checkExistsWithUsername(id, dto.getUsername())) {
            throw new RuntimeException("telegram bot already exits username");
        }
        TelegramBotDTO entity = findById(id);
        entity.setUsername(dto.getUsername());
        entity.setToken(dto.getToken());
        entity.setWebhookPath(dto.getWebhookPath());
        entity.setHost(dto.getHost());

        repository.save(entity);
        return entity;
    }

    public TelegramBotDTO get(UUID id) {
        return findById(id);
    }

    public List<TelegramBotDTO> getAll() {
        return null;
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public TelegramStatusDTO register(UUID id) {
        TelegramBotDTO entity = findById(id);
        telegramBotManager.registerNewBot(
                entity.getUsername(),
                entity.getToken(),
                entity.getWebhookPath(),
                entity.getHost()
        );
        entity.setRunning(true);
        repository.save(entity);
        return new TelegramStatusDTO();
    }

    public void registerAllBot() {
        List<TelegramBotDTO> entities = repository.findAllByBlockedFalse();
        for (TelegramBotDTO entity : entities) {
            try {
                telegramBotManager.registerNewBot(
                        entity.getUsername(),
                        entity.getToken(),
                        entity.getWebhookPath(),
                        entity.getHost()
                );
                entity.setRunning(true);
            } catch (Exception e) {
                log.error(e.getMessage());
                entity.setRunning(false);
            }
        }
        repository.saveAll(entities);
    }

    public TelegramStatusDTO unregister(UUID id, boolean dropPendingUpdates) {
        TelegramBotDTO entity = findById(id);
        telegramBotManager.disableBot(entity.getUsername(), dropPendingUpdates);
        entity.setRunning(false);
        repository.save(entity);
        return new TelegramStatusDTO();
    }

    public void unregisterAllBot() {
        List<TelegramBotDTO> entities = repository.findAllByBlockedFalse();
        for (TelegramBotDTO entity : entities) {
            try {
                telegramBotManager.disableBot(entity.getUsername(), false);
                entity.setRunning(false);
            } catch (Exception ignore) {
                entity.setRunning(true);
            }
        }
        repository.saveAll(entities);
    }

    public TelegramStatusDTO healthcheck(UUID id) {
        TelegramBotDTO entity = findById(id);
        boolean botHealthy = telegramBotManager.isBotHealthy(entity.getUsername());
        return new TelegramStatusDTO(botHealthy);
    }

    public TelegramBotDTO block(UUID id) {
        TelegramBotDTO entity = findById(id);
        entity.setBlocked(Objects.nonNull(entity.getBlocked()) && !entity.getBlocked());
        repository.save(entity);
        return entity;
    }

    private TelegramBotDTO findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("telegram bot not found id"));
    }

    private boolean checkExistsWithUsername(String username) {
        return repository.existsByUsernameEqualsIgnoreCase(username);
    }

    private boolean checkExistsWithUsername(UUID id, String username) {
        return repository.existsByUsernameEqualsIgnoreCaseAndIdNot(username, id);
    }

}
