package com.guinardpouard.imposteur.domain.model;

public class PlayerId {

    private final String value;

    private PlayerId(String value) {
        this.value = value;
    }

    public static PlayerId of(String value) {
        return new PlayerId(value);
    }

    public String getValue() {
        return value;
    }
}
