package com.guinardpouard.imposteur.domain.model;

public class Player {

    private final String userId;
    private final String playerName;
    private final RoomRole role;

    private Player(String userId, String playerName, RoomRole role) {
        this.userId = userId;
        this.playerName = playerName;
        this.role = role;
    }

    public static Player player(String userId, String playerName) {
        return new Player(userId, playerName, RoomRole.PLAYER);
    }

    public static Player host(String userId, String playerName) {
        return new Player(userId, playerName, RoomRole.HOST);
    }

    public String getUserId() {
        return userId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public RoomRole getRole() {
        return role;
    }
}
