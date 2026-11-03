package com.challengeFase2.fiap.infrastructure.web.dto;

import com.challengeFase2.fiap.application.command.UserTypeCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UserTypeRequest(
        @Schema(example = "Dono de Restaurante") @NotBlank String name) {

    public UserTypeCommand toCommand() {
        return new UserTypeCommand(name);
    }
}
