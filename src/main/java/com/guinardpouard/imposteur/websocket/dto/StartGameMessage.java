package com.guinardpouard.imposteur.websocket.dto;

public record StartGameMessage(
        String roomId,
        String hostId
) {
}
