package com.guinardpouard.imposteur.domain.model;

public class Player {

    private final PlayerId id;
    private final String name;

    private Player(String playerId, String name) {
        this.id = PlayerId.of(playerId);
        this.name = name;
    }

    public static Player player(String playerId, String playerName) {
        return new Player(playerId, playerName);
    }

    public PlayerId id() {
        return id;
    }

    public String getName() {
        return name;
    }
}
