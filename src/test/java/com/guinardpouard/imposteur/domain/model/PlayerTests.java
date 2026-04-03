package com.guinardpouard.imposteur.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerTests {

    @Test
    void a_player_should_have_a_player_id_a_webSocket_connectionId_and_a_name() {
        Player player = Player.player("connection-1", "player1");

        assertThat(player.id()).isNotNull();
        assertThat(player.getName()).isEqualTo("player1");
        assertThat(player.id().getValue()).isEqualTo("connection-1");
    }
}
