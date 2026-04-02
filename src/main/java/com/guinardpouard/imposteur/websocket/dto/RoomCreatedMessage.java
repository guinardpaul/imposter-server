package com.guinardpouard.imposteur.websocket.dto;

public record RoomCreatedMessage(
        String roomId,
        String roomName
) {}

