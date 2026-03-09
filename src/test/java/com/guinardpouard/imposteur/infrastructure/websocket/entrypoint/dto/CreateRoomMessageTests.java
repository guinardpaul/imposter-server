package com.guinardpouard.imposteur.infrastructure.websocket.entrypoint.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateRoomMessageTests {

    @Test
    void roomCreatedMessage_should_contains_id_and_name() {
        CreateRoomMessage roomCreatedMessage = new CreateRoomMessage("playerId");
        assertThat(roomCreatedMessage.roomName()).isEqualTo("playerId");
    }
}
