package com.guinardpouard.imposteur.domain.model;

import java.util.List;

public class InProgressState implements GameState {

    static final InProgressState INSTANCE = new InProgressState();
    private InProgressState() {
    }

    @Override
    public GameState join(Room room, Player player) {
        throw new IllegalStateException("Game has already started");
    }

    @Override
    public GameState start(Room room, List<Player> players, String hostId) {
        throw new IllegalStateException("Game has already started");
    }

    @Override
    public GameState end(Room room) {
        return FinishedState.INSTANCE;
    }

    @Override
    public GamePhase state() {
        return GamePhase.IN_PROGRESS;
    }
}
