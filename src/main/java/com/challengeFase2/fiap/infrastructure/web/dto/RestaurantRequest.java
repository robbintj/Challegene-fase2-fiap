package com.challengeFase2.fiap.infrastructure.web.dto;

import com.challengeFase2.fiap.application.command.RestaurantCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record RestaurantRequest(
        @Schema(example = "Cantina Bella") @NotBlank String name,
        @Schema(example = "Rua das Flores, 100 - São Paulo/SP") @NotBlank String address,
        @Schema(example = "Italiana") @NotBlank String cuisineType,
        @Schema(example = "11:00", type = "string") @NotNull LocalTime opensAt,
        @Schema(example = "23:00", type = "string") @NotNull LocalTime closesAt,
        @Schema(example = "1") @NotNull Long ownerId) {

    public RestaurantCommand toCommand() {
        return new RestaurantCommand(name, address, cuisineType, opensAt, closesAt, ownerId);
    }
}
