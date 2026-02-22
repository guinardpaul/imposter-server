package com.guinardpouard.imposteur.domain.model;

public class FinishedState implements GameState {

    static final FinishedState INSTANCE = new FinishedState();
    private FinishedState() {}

    @Override
    public GameState join(Room room, Player player) {
        room.addPlayer(player);
        return this;
    }

    @Override
    public GameState start(Room room) {
        if (room.getPlayers().size() < 2) {
            throw new IllegalStateException("Game cannot start with less than 2 players");
        }
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
