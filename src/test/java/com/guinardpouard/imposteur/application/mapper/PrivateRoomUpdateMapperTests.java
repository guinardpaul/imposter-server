package com.guinardpouard.imposteur.application.mapper;

import com.guinardpouard.imposteur.application.event.PrivateRoomUpdatedMessage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PrivateRoomUpdateMapperTests {

    @Test
    void should_create_privateRoomUpdateMessage_from_domain() {
        PrivateRoomUpdateMapper mapper = new PrivateRoomUpdateMapper();
        PrivateRoomUpdatedMessage msg = mapper.toMessage("room id", "player id", "Avion");

        assertThat(msg.roomId()).isEqualTo("room id");
        assertThat(msg.playerId()).isEqualTo("player id");
        assertThat(msg.word()).isEqualTo("Avion");
    }
}
