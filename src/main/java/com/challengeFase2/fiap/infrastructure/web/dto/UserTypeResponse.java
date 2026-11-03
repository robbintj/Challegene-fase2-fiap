package com.challengeFase2.fiap.infrastructure.web.dto;

import com.challengeFase2.fiap.domain.model.UserType;

public record UserTypeResponse(Long id, String name) {

    public static UserTypeResponse from(UserType type) {
        return new UserTypeResponse(type.getId(), type.getName());
    }
}
