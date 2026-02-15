package com.guinardpouard.imposteur.websocket.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoomJoinMessageTests {

    @Test
    void should_contains_room_id_and_name_of_player_who_joined() {
        RoomJoinMessage msg = new RoomJoinMessage("room1", "player1");
        assertThat(msg.roomId()).isEqualTo("room1");
        assertThat(msg.playerName()).isEqualTo("player1");
    }
}
