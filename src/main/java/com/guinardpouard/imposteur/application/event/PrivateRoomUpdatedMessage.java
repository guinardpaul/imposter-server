package com.guinardpouard.imposteur.application.event;

public record PrivateRoomUpdatedMessage(
        String roomId,
        String playerId,
        String word
) {
}
