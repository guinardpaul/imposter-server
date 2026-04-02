package com.guinardpouard.imposteur.websocket.mapper;

import com.guinardpouard.imposteur.domain.model.Player;
import com.guinardpouard.imposteur.domain.model.Room;
import com.guinardpouard.imposteur.websocket.dto.RoomUpdatedMessage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RoomUpdatedMapperTests {


    @Test
    void toMessage_should_create_msg_from_Room_domain() {
        Room room = new Room("room1");
        room.join(Player.host("user-1", "player1"));
        room.join(Player.player("user-2", "player2"));

        RoomUpdatedMapper roomUpdatedMapper = new RoomUpdatedMapper();
        RoomUpdatedMessage msg = roomUpdatedMapper.toMessage(room);

        assertThat(msg.roomId()).isEqualTo(room.getRoomId());
        assertThat(msg.roomName()).isEqualTo(room.getRoomName());
        assertThat(msg.playerViewList()).hasSize(2);
    }
}
