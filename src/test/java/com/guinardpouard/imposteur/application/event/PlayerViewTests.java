package com.guinardpouard.imposteur.application.event;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerViewTests {

    @Test
    void playerView_should_contains_id_and_name() {
        PlayerView playerView = new PlayerView("playerId", "playerName");
        assertThat(playerView.playerId()).isEqualTo("playerId");
        assertThat(playerView.playerName()).isEqualTo("playerName");
    }
}
