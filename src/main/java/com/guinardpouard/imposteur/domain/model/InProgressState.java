package com.guinardpouard.imposteur.domain.model;

public class InProgressState implements GameState {

    static final InProgressState INSTANCE = new InProgressState();
    private InProgressState() {
    }

    @Override
    public GameState join(Room room, Player player) {
        throw new IllegalStateException("Game has already started");
    }

    @Override
    public GameState start(Room room) {
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
