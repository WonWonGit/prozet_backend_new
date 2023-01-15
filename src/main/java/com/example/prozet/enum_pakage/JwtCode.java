package com.example.prozet.enum_pakage;

public enum JwtCode {
    ACCESS("access"),
    EXPIRED("expired"),
    DENINED("denined");

    private final String status;

    JwtCode(String status) {
        this.status = status;
    }

    public String status() {
        return status;
    }
}
