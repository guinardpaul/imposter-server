package com.guinardpouard.imposteur.domain.model;

import java.util.List;

public class FinishedState implements GameState {

    static final FinishedState INSTANCE = new FinishedState();
    private FinishedState() {}

    @Override
    public GameState join(Room room, Player player) {
        room.addPlayer(player);
        return this;
    }

    @Override
    public GameState start(Room room, List<Player> players, String hostId, WordPair wordPair) {
        room.start(players, hostId, wordPair);
        return InProgressState.INSTANCE;
    }

    @Override
    public GameState end(Room room) {
        throw new IllegalStateException("Game has already ended");
    }

    @Override
    public GamePhase state() {
        return GamePhase.FINISHED;
    }
}
