package com.example.prozet.enum_pakage;

public enum Provider {
    GOOGLE("google");

    private final String provider;

    Provider(String provider) {
        this.provider = provider;
    }

    public String provider(){
        return provider;
    }
}
