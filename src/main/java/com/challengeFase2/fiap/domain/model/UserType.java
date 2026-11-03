package com.challengeFase2.fiap.domain.model;

public class UserType {

    public static final String RESTAURANT_OWNER = "Dono de Restaurante";
    public static final String CUSTOMER = "Cliente";

    private final Long id;
    private String name;

    public UserType(Long id, String name) {
        this.id = id;
        this.name = Guard.requireText(name, "Nome do tipo");
    }

    public void rename(String name) {
        this.name = Guard.requireText(name, "Nome do tipo");
    }

    public boolean isRestaurantOwner() {
        return RESTAURANT_OWNER.equalsIgnoreCase(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
