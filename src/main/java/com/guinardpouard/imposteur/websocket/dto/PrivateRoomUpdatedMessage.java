package com.guinardpouard.imposteur.websocket.dto;

public record PrivateRoomUpdatedMessage(
        String roomId,
        String roomName,
        String word
) {
}
