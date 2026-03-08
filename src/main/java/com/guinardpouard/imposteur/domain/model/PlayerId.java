package com.guinardpouard.imposteur.domain.model;

import java.util.UUID;

public class PlayerId {

    private final String value;

    private PlayerId(String value) {
        this.value = value;
    }

    public static PlayerId newId() {
        return new PlayerId(UUID.randomUUID().toString());
    }

    public static PlayerId of(String value) {
        return new PlayerId(value);
    }

    public String getValue() {
        return value;
    }
}
