package com.guinardpouard.imposteur.websocket.mapper;

import com.guinardpouard.imposteur.domain.model.Room;
import com.guinardpouard.imposteur.websocket.dto.PrivateRoomUpdatedMessage;
import org.springframework.stereotype.Component;

@Component
public class GameStartedMapper {

    public PrivateRoomUpdatedMessage toMessage(Room room) {
        return new PrivateRoomUpdatedMessage(
                room.getRoomId(),
                room.getRoomName(),
                "First word"
        );
    }
}
