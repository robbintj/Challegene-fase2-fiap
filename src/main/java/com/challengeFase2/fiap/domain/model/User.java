package com.challengeFase2.fiap.domain.model;

public class User {

    private final Long id;
    private String name;
    private String email;
    private Long userTypeId;

    public User(Long id, String name, String email, Long userTypeId) {
        this.id = id;
        update(name, email, userTypeId);
    }

    public void update(String name, String email, Long userTypeId) {
        this.name = Guard.requireText(name, "Nome");
        this.email = Guard.requireText(email, "E-mail").toLowerCase();
        this.userTypeId = Guard.requireNonNull(userTypeId, "Tipo de usuário");
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Long getUserTypeId() {
        return userTypeId;
    }
}
