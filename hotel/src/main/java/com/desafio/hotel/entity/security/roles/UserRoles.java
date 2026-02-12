package com.desafio.hotel.entity.security.roles;

import lombok.Getter;

@Getter
public enum UserRoles {
    USER("user"),
    ADMIN("admin");

    private final String role;

    UserRoles(String role) {
        this.role = role;
    }
}
