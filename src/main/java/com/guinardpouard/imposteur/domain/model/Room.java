package com.guinardpouard.imposteur.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Room {

    private final String roomId;
    private final String roomName;
    private final List<Player> players;
    private GameState state;

    public Room(String roomName) {
        this.roomId = UUID.randomUUID().toString();
        this.roomName = roomName;
        this.players = new ArrayList<>();

        state = JoiningState.INSTANCE;
    }

    public void startGame() {
        state = state.start(this);
    }

    public void join(Player player) {
        state = state.join(this, player);
    }

    public void endGame() {
        state = state.end(this);
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public List<Player> getPlayers() {
        return List.copyOf(players);
    }

    void addPlayer(Player player) {
        players.add(player);
    }

    GameState getState() {
        return state;
    }
}
