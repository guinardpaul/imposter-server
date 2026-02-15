package com.guinardpouard.imposteur.domain.model;

import java.util.UUID;

public class Player {

    private final String playerId;
    private final String playerName;

    public Player(String playerName) {
        this.playerId = UUID.randomUUID().toString();
        this.playerName = playerName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

}
