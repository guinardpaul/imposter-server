package com.guinardpouard.imposteur.application.mapper;

import com.guinardpouard.imposteur.application.event.PrivateRoomUpdatedMessage;
import org.springframework.stereotype.Component;

@Component
public class PrivateRoomUpdateMapper {

    public PrivateRoomUpdatedMessage toMessage(String roomId, String playerId, String word) {
        return new PrivateRoomUpdatedMessage(
                roomId,
                playerId,
                word
        );
    }
}
