package com.guinardpouard.imposteur.application.event;

import java.util.List;

public record RoomUpdatedMessage(
        String roomId,
        String roomName,
        List<PlayerView> playerViewList
) {
}
