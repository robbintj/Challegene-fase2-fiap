package com.challengeFase2.fiap.infrastructure.web.dto;

import com.challengeFase2.fiap.domain.model.Restaurant;

import java.time.LocalTime;

public record RestaurantResponse(Long id, String name, String address, String cuisineType,
                                 LocalTime opensAt, LocalTime closesAt, Long ownerId) {

    public static RestaurantResponse from(Restaurant r) {
        return new RestaurantResponse(r.getId(), r.getName(), r.getAddress(), r.getCuisineType(),
                r.getOpeningHours().opensAt(), r.getOpeningHours().closesAt(), r.getOwnerId());
    }
}
