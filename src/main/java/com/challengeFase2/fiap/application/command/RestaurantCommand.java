package com.challengeFase2.fiap.application.command;

import java.time.LocalTime;

public record RestaurantCommand(String name, String address, String cuisineType,
                                LocalTime opensAt, LocalTime closesAt, Long ownerId) {
}
