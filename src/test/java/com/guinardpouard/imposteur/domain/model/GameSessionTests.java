package com.guinardpouard.imposteur.domain.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GameSessionTests {

    @Test
    void gameSession_can_start_a_new_round() {
        Player p1 = Player.player("u1", "p1");
        Player p2 = Player.player("u2", "p2");
        Player p3 = Player.player("u3","p3");
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);

        GameSession gameSession = new GameSession(players);
        gameSession.startNextRound(new WordPair("a", "b"));
        assertThat(gameSession.getCurrentRound()).isNotNull();

        gameSession.startNextRound(new WordPair("Cheval", "Voiture"));
        assertThat(gameSession.getCurrentRound()).isNotNull();
    }
}
