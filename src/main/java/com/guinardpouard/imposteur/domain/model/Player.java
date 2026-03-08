package com.guinardpouard.imposteur.domain.model;

import java.util.UUID;

public class Player {

    private final PlayerId id;
    private final String name;

    private String connectionId;

    private Player(String connectionId, String name) {
        this.id = PlayerId.newId();
        this.name = name;

        connect(connectionId);
    }

    private Player(String connectionId) {
        this.id = PlayerId.newId();
        this.name = UUID.randomUUID().toString();

        connect(connectionId);
    }

    public static Player player(String connectionId, String playerName) {
        return new Player(connectionId, playerName);
    }

    public static Player player(String connectionId) {
        return new Player(connectionId);
    }

    public void connect(String connectionId) {
        this.connectionId = connectionId;
    }

    public void disconnect() {
        this.connectionId = null;
    }

    public boolean isConnected() {
        return connectionId != null;
    }

    public PlayerId id() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getConnectionId() {
        return connectionId;
    }
}
