package com.guinardpouard.imposteur.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerTests {

    @Test
    void a_player_should_have_a_random_id_and_a_name() {
        Player player = new Player("player1");
        assertThat(player.getPlayerId()).isNotNull();
        assertThat(player.getPlayerName()).isNotNull().isEqualTo("player1");
    }

}
