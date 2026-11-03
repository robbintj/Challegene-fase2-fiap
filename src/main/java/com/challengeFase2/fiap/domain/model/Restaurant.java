package com.challengeFase2.fiap.domain.model;

public class Restaurant {

    private final Long id;
    private String name;
    private String address;
    private String cuisineType;
    private OpeningHours openingHours;
    private Long ownerId;

    public Restaurant(Long id, String name, String address, String cuisineType,
                      OpeningHours openingHours, Long ownerId) {
        this.id = id;
        update(name, address, cuisineType, openingHours, ownerId);
    }

    public void update(String name, String address, String cuisineType,
                       OpeningHours openingHours, Long ownerId) {
        this.name = Guard.requireText(name, "Nome");
        this.address = Guard.requireText(address, "Endereço");
        this.cuisineType = Guard.requireText(cuisineType, "Tipo de cozinha");
        this.openingHours = Guard.requireNonNull(openingHours, "Horário de funcionamento");
        this.ownerId = Guard.requireNonNull(ownerId, "Dono do restaurante");
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public Long getOwnerId() {
        return ownerId;
    }
}
