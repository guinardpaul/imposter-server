package com.guinardpouard.imposteur.websocket.dto;

import java.util.List;

public record RoomUpdatedMessage(
        String roomId,
        String roomName,
        List<PlayerView> playerViewList
) {
}
