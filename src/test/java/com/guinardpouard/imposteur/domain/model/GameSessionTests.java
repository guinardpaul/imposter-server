package com.guinardpouard.imposteur.domain.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GameSessionTests {

    @Test
    void gameSession_should_define_roles_for_each_players() {
        Player p1 = Player.player("u1", "p1");
        Player p2 = Player.player("u2", "p2");
        Player p3 = Player.player("u3","p3");
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);

        GameSession gameSession = new GameSession(players);
        long impostorCount = players.stream().filter(gameSession::isImpostor).count();
        assertThat(impostorCount).isEqualTo(1);

        long crewmates = players.stream()
                .filter(p -> !gameSession.isImpostor(p))
                .count();
        assertThat(crewmates).isEqualTo(players.size() - 1);
    }
}
