package com.ixtiyor.tgwebhookbotcore.dto.telegram;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelegramBotDTO {
    private UUID id = UUID.randomUUID();
    private String username;
    private String token;
    private String webhookPath;
    private String host;
    private Boolean running;
    private Boolean blocked;

    public TelegramBotDTO(String username, String token, String webhookPath, String host) {
        this.username = username;
        this.token = token;
        this.webhookPath = webhookPath;
        this.host = host;
    }
}
