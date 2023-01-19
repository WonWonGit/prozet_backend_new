package com.example.prozet.enum_pakage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    MANAGER("ROLE_MANAGER");

    private final String role;

    private Role(String role) {
        this.role = role;
    }

    @JsonValue
    public String role() {
        return role;
    }

    @JsonCreator
    public static Role from(String role) {
        return Role.valueOf(role.toUpperCase());
    }

}
