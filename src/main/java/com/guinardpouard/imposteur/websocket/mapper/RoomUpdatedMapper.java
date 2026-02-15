package com.guinardpouard.imposteur.websocket.mapper;

import com.guinardpouard.imposteur.domain.model.Player;
import com.guinardpouard.imposteur.domain.model.Room;
import com.guinardpouard.imposteur.websocket.dto.PlayerView;
import com.guinardpouard.imposteur.websocket.dto.RoomUpdatedMessage;
import org.springframework.stereotype.Component;

@Component
public class RoomUpdatedMapper {

    public RoomUpdatedMessage toMessage(Room room) {
        return new RoomUpdatedMessage(
                room.getRoomId(),
                room.getRoomName(),
                room.getPlayers().stream().map(this::toPlayerView).toList());
    }

    private PlayerView toPlayerView(Player player) {
        return new PlayerView(
                player.getPlayerId(),
                player.getPlayerName()
        );
    }
}
