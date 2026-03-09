package com.guinardpouard.imposteur.infrastructure.websocket.entrypoint.dto;

public record StartGameMessage(
        String roomId,
        String hostId
) {
}
