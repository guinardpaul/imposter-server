package com.guinardpouard.imposteur.domain.model;

import java.util.List;

public interface GameState {
    GameState join(Room room, Player player);
    GameState start(Room room, List<Player> players, String hostId, WordPair wordPair);
    GameState end(Room room);
    GamePhase state();
}
