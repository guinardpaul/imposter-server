package com.guinardpouard.imposteur.domain.model;

public final class JoiningState implements GameState {

    static final JoiningState INSTANCE = new JoiningState();

    private JoiningState() {}

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
        return FinishedState.INSTANCE;
    }

    @Override
    public GamePhase state() {
        return GamePhase.JOINING;
    }

}
