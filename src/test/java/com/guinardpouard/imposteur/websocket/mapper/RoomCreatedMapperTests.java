package com.guinardpouard.imposteur.websocket.mapper;

import com.guinardpouard.imposteur.domain.model.Room;
import com.guinardpouard.imposteur.websocket.dto.RoomCreatedMessage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RoomCreatedMapperTests {

    @Test
    void toMessage_should_create_msg_from_Room_domain() {
        Room room = new Room("room1");

        RoomCreatedMapper mapper = new RoomCreatedMapper();
        RoomCreatedMessage msg = mapper.toMessage(room);

        assertThat(msg.roomId()).isEqualTo(room.getRoomId());
        assertThat(msg.roomName()).isEqualTo(room.getRoomName());
    }
}
