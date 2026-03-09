package com.guinardpouard.imposteur.domain.model;

import java.util.List;

public final class JoiningState implements GameState {

    static final JoiningState INSTANCE = new JoiningState();

    private JoiningState() {}

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
        return FinishedState.INSTANCE;
    }

    @Override
    public GamePhase state() {
        return GamePhase.JOINING;
    }

}
