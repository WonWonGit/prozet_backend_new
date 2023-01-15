package com.example.prozet.enum_pakage;

public enum StateType {

    PANDING("PANDING"),
    ACCEPTED("ACCEPTED");

    private final String state;

    private StateType(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

}
