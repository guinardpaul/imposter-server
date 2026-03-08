package com.guinardpouard.imposteur.domain.model;

import java.util.*;

public class Room {

    private final String roomId;
    private final String roomName;
    private final String hostConnectionId;
    private final Map<PlayerId, Player> players;
    private GameSession currentGame;
    private GameState state;

    public Room(String roomName, String hostConnectionId) {
        this.roomId = UUID.randomUUID().toString();
        this.roomName = roomName;
        this.hostConnectionId = hostConnectionId;
        this.players = new HashMap<>();

        state = JoiningState.INSTANCE;
    }

    public void startGame(String hostId, WordPair wordPair) {
        state = state.start(this, players.values().stream().toList(), hostId, wordPair);
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
        return List.copyOf(players.values());
    }

    void addPlayer(Player player) {
        players.put(player.id(), player);
    }

    GameState getState() {
        return state;
    }

    public GameSession getCurrentGame() {
        if (currentGame == null) {
            throw new IllegalStateException("Game not started yet");
        }
        return currentGame;
    }

    void setCurrentGame(GameSession gameSession) {
        this.currentGame = gameSession;
    }

    boolean isHost(String connectionId) {
        return this.hostConnectionId.equals(connectionId);
    }
}
