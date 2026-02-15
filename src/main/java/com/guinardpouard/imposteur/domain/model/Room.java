package com.guinardpouard.imposteur.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Room {

    private final String roomId;
    private final String roomName;
    private final List<Player> players;

    public Room(String roomName) {
        this.roomId = UUID.randomUUID().toString();
        this.roomName = roomName;
        this.players = new ArrayList<>();
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
}
