package com.challengeFase2.fiap.infrastructure.web.dto;

import com.challengeFase2.fiap.application.command.UserCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @Schema(example = "Maria Silva") @NotBlank String name,
        @Schema(example = "maria@email.com") @NotBlank @Email String email,
        @Schema(example = "1") @NotNull Long userTypeId) {

    public UserCommand toCommand() {
        return new UserCommand(name, email, userTypeId);
    }
}
