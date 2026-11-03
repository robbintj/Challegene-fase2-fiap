package com.challengeFase2.fiap.domain.model;

import java.math.BigDecimal;

public class MenuItem {

    private final Long id;
    private final Long restaurantId;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean dineInOnly;
    private String photoPath;

    public MenuItem(Long id, Long restaurantId, String name, String description,
                    BigDecimal price, boolean dineInOnly, String photoPath) {
        this.id = id;
        this.restaurantId = Guard.requireNonNull(restaurantId, "Restaurante");
        update(name, description, price, dineInOnly, photoPath);
    }

    public void update(String name, String description, BigDecimal price,
                       boolean dineInOnly, String photoPath) {
        this.name = Guard.requireText(name, "Nome");
        this.description = Guard.requireText(description, "Descrição");
        this.price = Guard.requirePositive(price, "Preço");
        this.dineInOnly = dineInOnly;
        this.photoPath = Guard.requireText(photoPath, "Caminho da foto");
    }

    public Long getId() {
        return id;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isDineInOnly() {
        return dineInOnly;
    }

    public String getPhotoPath() {
        return photoPath;
    }
}
