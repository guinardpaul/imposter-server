package com.guinardpouard.imposteur.websocket.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoomCreatedMessageTests {

    @Test
    void roomCreatedMessage_should_contains_id_and_name() {
        RoomCreatedMessage roomCreatedMessage = new RoomCreatedMessage("roomId", "roomName");
        assertThat(roomCreatedMessage.roomId()).isEqualTo("roomId");
        assertThat(roomCreatedMessage.roomName()).isEqualTo("roomName");
    }
}
