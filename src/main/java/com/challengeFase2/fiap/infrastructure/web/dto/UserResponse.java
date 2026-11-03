package com.challengeFase2.fiap.infrastructure.web.dto;

import com.challengeFase2.fiap.domain.model.User;

public record UserResponse(Long id, String name, String email, Long userTypeId) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getUserTypeId());
    }
}
