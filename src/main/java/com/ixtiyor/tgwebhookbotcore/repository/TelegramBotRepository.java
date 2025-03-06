package com.ixtiyor.tgwebhookbotcore.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ixtiyor.tgwebhookbotcore.dto.telegram.TelegramBotDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TelegramBotRepository {
    private final String filePath = "src/main/resources/telegram_bots.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<TelegramBotDTO> readJsonFile() {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<TelegramBotDTO>>() {
            });
        } catch (IOException e) {
            log.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    private void writeJsonFile(List<TelegramBotDTO> bots) {
        try {
            objectMapper.writeValue(new File(filePath), bots);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void save(TelegramBotDTO entity) {
        List<TelegramBotDTO> bots = readJsonFile();
        Optional<TelegramBotDTO> existing = bots.stream()
                .filter(bot -> bot.getId().equals(entity.getId()))
                .findFirst();
        existing.ifPresent(bots::remove);
        bots.add(entity);
        writeJsonFile(bots);
    }

    public void deleteById(UUID id) {
        List<TelegramBotDTO> bots = readJsonFile();
        bots = bots.stream().filter(bot -> !bot.getId().equals(id)).collect(Collectors.toList());
        writeJsonFile(bots);
    }

    public List<TelegramBotDTO> findAllByBlockedFalse() {
        return readJsonFile().stream()
                .filter(bot -> Objects.nonNull(bot.getBlocked()) && !bot.getBlocked())
                .collect(Collectors.toList());
    }

    public void saveAll(List<TelegramBotDTO> entities) {
        List<TelegramBotDTO> bots = readJsonFile();
        for (TelegramBotDTO entity : entities) {
            Optional<TelegramBotDTO> existing = bots.stream()
                    .filter(bot -> bot.getId().equals(entity.getId()))
                    .findFirst();
            existing.ifPresent(bots::remove);
            bots.add(entity);
        }
        writeJsonFile(bots);
    }

    public Optional<TelegramBotDTO> findById(UUID id) {
        return readJsonFile().stream()
                .filter(bot -> bot.getId().equals(id))
                .findFirst();
    }

    public boolean existsByUsernameEqualsIgnoreCase(String username) {
        return readJsonFile().stream()
                .anyMatch(bot -> bot.getUsername().equalsIgnoreCase(username));
    }

    public boolean existsByUsernameEqualsIgnoreCaseAndIdNot(String username, UUID id) {
        return readJsonFile().stream()
                .anyMatch(bot -> bot.getUsername().equalsIgnoreCase(username) && !bot.getId().equals(id));
    }
}
