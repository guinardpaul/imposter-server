package com.guinardpouard.imposteur.domain.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RoundTests {

    @Test
    void a_round_can_be_created_with_a_list_of_players() {
        Round round = Round.create(createPlayerList(), new WordPair("Pomme", "Avion"));

        assertThat(round).isNotNull();
    }


    @Test
    void should_contains_all_players() {
        List<Player> players = createPlayerList();

        Round round = Round.create(players, new WordPair("Pomme", "Avion"));

        for (int i = 0; i < 4; i++) {
            Player player = players.get(i);
            assertThat(round.getStateFor(player.id())).isNotNull();
            assertThat(round.getStateFor(player.id()).playerId()).isEqualTo(player.id());
            assertThat(round.getStateFor(player.id()).word()).isNotBlank();
        }

        long impostors = players.stream()
                .filter(p -> round.isImpostor(p.id()))
                .count();
        assertThat(impostors).isEqualTo(1);
    }

    @Test
    void should_contains_only_one_impostor() {
        List<Player> players = createPlayerList();

        Round round = Round.create(players, new WordPair("Pomme", "Avion"));

        long impostors = players.stream()
                .filter(p -> round.isImpostor(p.id()))
                .count();
        assertThat(impostors).isEqualTo(1);

        long crewmate = players.stream().filter(p -> !round.isImpostor(p.id())).count();
        assertThat(crewmate).isEqualTo(3);
    }

    @Test
    void impostor_should_have_specific_word() {
        List<Player> players = createPlayerList();

        Round round = Round.create(players, new WordPair("Pomme", "Avion"));

        long crewmate = players.stream()
                .filter(p -> round.getStateFor(p.id()).word().equals("Pomme") &&
                 !round.isImpostor(p.id()))
                .count();
        assertThat(crewmate).isEqualTo(3);

        long impostor = players.stream().filter(p -> round.isImpostor(p.id()) && round.getStateFor(p.id()).word().equals("Avion")).count();
        assertThat(impostor).isEqualTo(1);
    }


    private List<Player> createPlayerList() {
        Player p1 = Player.player("user1");
        Player p2 = Player.player("user2");
        Player p3 = Player.player("user3");
        Player p4 = Player.player("user4");

        return Arrays.asList(p1, p2, p3, p4);
    }
}
