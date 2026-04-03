package com.guinardpouard.imposteur.infrastructure.websocket.entrypoint.dto;

public record UpdateGameMessage(
        String roomId,
        String hostId
) {
}
