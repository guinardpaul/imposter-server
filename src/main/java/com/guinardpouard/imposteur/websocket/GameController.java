package com.guinardpouard.imposteur.websocket;

import com.guinardpouard.imposteur.domain.model.Room;
import com.guinardpouard.imposteur.websocket.dto.RoomJoinMessage;
import com.guinardpouard.imposteur.application.GameService;
import com.guinardpouard.imposteur.websocket.mapper.RoomCreatedMapper;
import com.guinardpouard.imposteur.websocket.mapper.RoomUpdatedMapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    private final SimpMessagingTemplate template;
    private final GameService gameService;
    private final RoomUpdatedMapper roomUpdatedMapper;
    private final RoomCreatedMapper roomCreatedMapper;

    public GameController(SimpMessagingTemplate template, GameService gameService, RoomUpdatedMapper roomUpdatedMapper, RoomCreatedMapper roomCreatedMapper) {
        this.template = template;
        this.gameService = gameService;
        this.roomUpdatedMapper = roomUpdatedMapper;
        this.roomCreatedMapper = roomCreatedMapper;
    }

    @MessageMapping("/room.create")
    public void create() {
        Room room = gameService.createRoom();
        template.convertAndSend(
                "/topic/room",
                roomCreatedMapper.toMessage(room)
        );
    }

    @MessageMapping("/room.join")
    public void join(RoomJoinMessage msg) {
        Room room = gameService.addPlayerToRoom(msg.roomId(), msg.playerName());
        template.convertAndSend(
                "/topic/room/" + msg.roomId() + "/players",
                roomUpdatedMapper.toMessage(room)
        );
    }

}
