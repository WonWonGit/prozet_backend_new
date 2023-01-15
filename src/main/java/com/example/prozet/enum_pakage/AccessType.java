package com.example.prozet.enum_pakage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AccessType {

    READONLY("READONLY"),
    EDIT("EDIT");

    private AccessType(String access) {
        this.access = access;
    }

    private final String access;

    @JsonValue
    public String getAccess() {
        return access;
    }

    @JsonCreator
    public static AccessType from(String access) {
        return AccessType.valueOf(access.toUpperCase());
    }

}
