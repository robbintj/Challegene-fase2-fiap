package com.challengeFase2.fiap.infrastructure.web.dto;

import com.challengeFase2.fiap.application.command.MenuItemCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MenuItemRequest(
        @Schema(example = "Lasanha à Bolonhesa") @NotBlank String name,
        @Schema(example = "Lasanha com molho bolonhesa e queijo gratinado") @NotBlank String description,
        @Schema(example = "42.90") @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal price,
        @Schema(example = "false") @NotNull Boolean dineInOnly,
        @Schema(example = "/images/lasanha.jpg") @NotBlank String photoPath) {

    public MenuItemCommand toCommand() {
        return new MenuItemCommand(name, description, price, dineInOnly, photoPath);
    }
}
