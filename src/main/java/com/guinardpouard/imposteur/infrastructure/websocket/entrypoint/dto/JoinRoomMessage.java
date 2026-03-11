package com.guinardpouard.imposteur.infrastructure.websocket.entrypoint.dto;

public record JoinRoomMessage(
        String roomId,
        String playerName
) {
}
