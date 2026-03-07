package com.guinardpouard.imposteur.websocket.dto;

public record RoomCreateMessage(
        String roomName,
        String playerName
) {
}
