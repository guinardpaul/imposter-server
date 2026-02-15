package com.guinardpouard.imposteur.websocket.dto;

public record RoomJoinMessage(
        String roomId,
        String playerName
) {
}
