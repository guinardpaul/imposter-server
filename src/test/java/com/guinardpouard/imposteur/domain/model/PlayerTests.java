package com.guinardpouard.imposteur.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerTests {

    @Test
    void a_player_should_have_a_player_id_a_webSocket_connectionId_and_a_name() {
        Player player = Player.player("connection-1", "player1");

        assertThat(player.id()).isNotNull();
        assertThat(player.getName()).isEqualTo("player1");
        assertThat(player.getConnectionId()).isEqualTo("connection-1");
    }

    @Test
    void a_player_should_have_an_id_and_a_generated_name() {
        Player player = Player.player("connection-1");

        assertThat(player.id()).isNotNull();
        assertThat(player.getName()).isNotBlank();
        assertThat(player.getConnectionId()).isEqualTo("connection-1");
    }

    @Test
    void creating_a_player_should_connect_it() {
        Player player = Player.player("connection-1", "player1");

        assertThat(player.getConnectionId()).isNotNull();
        assertThat(player.isConnected()).isTrue();
    }

    @Test
    void player_disconnection_should_remove_its_connectionId() {
        Player player = Player.player("connection-1");
        assertThat(player.isConnected()).isTrue();

        player.disconnect();
        assertThat(player.isConnected()).isFalse();
    }

}
