package com.guinardpouard.imposteur.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerTests {

    @Test
    void a_player_should_have_an_id_and_a_name() {
        Player player = Player.player("user-1", "player1");

        assertThat(player.getUserId()).isNotNull();
        assertThat(player.getPlayerName()).isNotNull().isEqualTo("player1");
        assertThat(player.getRole()).isEqualTo(RoomRole.PLAYER);
    }

    @Test
    void a_host_should_have_an_id_and_a_name() {
        Player host = Player.host("user-1", "host1");

        assertThat(host.getUserId()).isNotNull().isEqualTo("user-1");
        assertThat(host.getPlayerName()).isEqualTo("host1");
        assertThat(host.getRole()).isEqualTo(RoomRole.HOST);
    }

}
