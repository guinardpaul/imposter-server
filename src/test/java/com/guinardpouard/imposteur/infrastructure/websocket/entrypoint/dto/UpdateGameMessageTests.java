package com.guinardpouard.imposteur.infrastructure.websocket.entrypoint.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateGameMessageTests {

    @Test
    void should_contains_dto() {
        UpdateGameMessage msg = new UpdateGameMessage(
                "room id",
                "hostConnectionId"
        );

        assertThat(msg.roomId()).isEqualTo("room id");
        assertThat(msg.hostId()).isEqualTo("hostConnectionId");
    }
}
