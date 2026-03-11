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

    void start(List<Player> players, String hostId, WordPair wordPair) {
        if (players.size() < 2) {
            throw new IllegalStateException("Game cannot start with less than 2 players");
        }
        if (!isHost(hostId)) {
            throw new IllegalStateException("Game can be started only by host");
        }

        currentGame = new GameSession(players, wordPair);
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

    boolean isHost(String connectionId) {
        return this.hostConnectionId.equals(connectionId);
    }

    public void startNextRound(WordPair wordPair) {
        if (this.currentGame == null) {
            throw new IllegalStateException("Game not started");
        }
        this.currentGame.startNextRound(wordPair);
    }

    public Map<PlayerId, PlayerRoundState> getStates() {
        if (this.currentGame == null) {
            throw new IllegalStateException("Game not started");
        }
        return this.currentGame.getCurrentRound().getStates();
    }
}
