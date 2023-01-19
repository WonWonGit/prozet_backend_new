package com.example.prozet.enum_pakage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StackType {

    CUSTOMSTACK("CUSTOMSTACK"),
    DEFAULTSTACK("DEFAULTSTACK");

    private final String stackType;

    private StackType(String stackType) {
        this.stackType = stackType;
    }

    @JsonValue
    public String stackType() {
        return stackType;
    }

    @JsonCreator
    public static StackType from(String stackType) {
        return StackType.valueOf(stackType.toUpperCase());
    }
}
