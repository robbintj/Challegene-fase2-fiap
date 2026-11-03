package com.challengeFase2.fiap.infrastructure.web.dto;

import com.challengeFase2.fiap.domain.model.MenuItem;

import java.math.BigDecimal;

public record MenuItemResponse(Long id, Long restaurantId, String name, String description,
                               BigDecimal price, boolean dineInOnly, String photoPath) {

    public static MenuItemResponse from(MenuItem i) {
        return new MenuItemResponse(i.getId(), i.getRestaurantId(), i.getName(), i.getDescription(),
                i.getPrice(), i.isDineInOnly(), i.getPhotoPath());
    }
}
