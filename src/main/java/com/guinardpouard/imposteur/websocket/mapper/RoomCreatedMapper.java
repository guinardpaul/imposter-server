package com.guinardpouard.imposteur.websocket.mapper;

import com.guinardpouard.imposteur.domain.model.Room;
import com.guinardpouard.imposteur.websocket.dto.RoomCreatedMessage;
import org.springframework.stereotype.Component;

@Component
public class RoomCreatedMapper {

    public RoomCreatedMessage toMessage(Room room) {
        return new RoomCreatedMessage(
                room.getRoomId(),
                room.getRoomName()
        );
    }

}
