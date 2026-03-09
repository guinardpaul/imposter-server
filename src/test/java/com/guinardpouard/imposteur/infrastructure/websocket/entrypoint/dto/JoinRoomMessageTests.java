package com.guinardpouard.imposteur.infrastructure.websocket.entrypoint.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JoinRoomMessageTests {

    @Test
    void should_contains_room_id_and_name_of_player_who_joined() {
        JoinRoomMessage msg = new JoinRoomMessage("room1", "player1");
        assertThat(msg.roomId()).isEqualTo("room1");
        assertThat(msg.playerName()).isEqualTo("player1");
    }
}
