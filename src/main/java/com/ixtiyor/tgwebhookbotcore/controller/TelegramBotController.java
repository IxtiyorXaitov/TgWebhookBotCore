package com.ixtiyor.tgwebhookbotcore.controller;

import com.ixtiyor.tgwebhookbotcore.dto.http.ApiResult;
import com.ixtiyor.tgwebhookbotcore.dto.telegram.TelegramBotDTO;
import com.ixtiyor.tgwebhookbotcore.dto.telegram.TelegramStatusDTO;
import com.ixtiyor.tgwebhookbotcore.service.TelegramBotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/bot/telegram")
@RequiredArgsConstructor
public class TelegramBotController {
    private final TelegramBotService service;

    @Operation(summary = "Create Telegram Bot", description = "Creates a new Telegram bot with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Telegram bot created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ApiResult<TelegramBotDTO> create(
            @RequestBody TelegramBotDTO dto
    ) {
        TelegramBotDTO result = service.create(dto);
        return ApiResult.build(result);
    }

    @Operation(summary = "Update Telegram Bot", description = "Updates an existing Telegram bot identified by its UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Telegram bot updated successfully"),
            @ApiResponse(responseCode = "404", description = "Telegram bot not found")
    })
    @PutMapping("/{id}")
    public ApiResult<TelegramBotDTO> update(
            @PathVariable UUID id,
            @RequestBody TelegramBotDTO dto
    ) {
        TelegramBotDTO result = service.update(id, dto);
        return ApiResult.build(result);
    }

    @Operation(summary = "Get Telegram Bot", description = "Retrieves a Telegram bot by its UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Telegram bot retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Telegram bot not found")
    })
    @GetMapping("/{id}")
    public ApiResult<TelegramBotDTO> get(
            @PathVariable UUID id
    ) {
        TelegramBotDTO result = service.get(id);
        return ApiResult.build(result);
    }

    @Operation(summary = "Get All Telegram Bots", description = "Retrieves a list of all Telegram bots.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Telegram bots retrieved successfully")
    })
    @GetMapping
    public ApiResult<List<TelegramBotDTO>> getAll(
    ) {
        List<TelegramBotDTO> result = service.getAll();
        return ApiResult.build(result);
    }

    @Operation(summary = "Delete Telegram Bot", description = "Deletes a Telegram bot by its UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Telegram bot deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Telegram bot not found")
    })
    @DeleteMapping("/{id}")
    public ApiResult<?> delete(
            @PathVariable UUID id
    ) {
        service.delete(id);
        return ApiResult.build(null);
    }

    @Operation(summary = "Register Telegram Bot", description = "Registers the Telegram bot so that it can start receiving messages.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Telegram bot registered successfully"),
            @ApiResponse(responseCode = "404", description = "Telegram bot not found")
    })
    @PutMapping("/register/{id}")
    public ApiResult<TelegramStatusDTO> register(
            @PathVariable UUID id
    ) {
        TelegramStatusDTO result = service.register(id);
        return ApiResult.build(result);
    }

    @Operation(summary = "Unregister Telegram Bot", description = "Unregisters the Telegram bot. Optionally, pending updates can be dropped.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Telegram bot unregistered successfully"),
            @ApiResponse(responseCode = "404", description = "Telegram bot not found")
    })
    @PutMapping("/unregister/{id}")
    public ApiResult<TelegramStatusDTO> unregister(
            @PathVariable UUID id,
            @RequestParam(required = false) boolean dropPendingUpdates
    ) {
        TelegramStatusDTO result = service.unregister(id, dropPendingUpdates);
        return ApiResult.build(result);
    }

    @Operation(summary = "Telegram Bot Healthcheck", description = "Performs a healthcheck on the Telegram bot by its UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Healthcheck successful"),
            @ApiResponse(responseCode = "404", description = "Telegram bot not found")
    })
    @GetMapping("/healthcheck/{id}")
    public ApiResult<TelegramStatusDTO> healthcheck(
            @PathVariable UUID id
    ) {
        TelegramStatusDTO result = service.healthcheck(id);
        return ApiResult.build(result);
    }

    @Operation(summary = "Block Telegram Bot", description = "Blocks the Telegram bot by its UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Telegram bot blocked successfully"),
            @ApiResponse(responseCode = "404", description = "Telegram bot not found")
    })
    @GetMapping("/block/{id}")
    public ApiResult<TelegramBotDTO> block(
            @PathVariable UUID id
    ) {
        TelegramBotDTO result = service.block(id);
        return ApiResult.build(result);
    }
}
