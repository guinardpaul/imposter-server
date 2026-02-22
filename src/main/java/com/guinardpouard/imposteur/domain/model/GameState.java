package com.guinardpouard.imposteur.domain.model;

public interface GameState {
    GameState join(Room room, Player player);
    GameState start(Room room);
    GameState end(Room room);
    GamePhase state();
}
