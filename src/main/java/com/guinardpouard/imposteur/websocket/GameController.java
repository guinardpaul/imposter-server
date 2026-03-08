package com.guinardpouard.imposteur.websocket;

import com.guinardpouard.imposteur.application.GameService;
import com.guinardpouard.imposteur.domain.model.Room;
import com.guinardpouard.imposteur.websocket.dto.RoomCreateMessage;
import com.guinardpouard.imposteur.websocket.dto.RoomJoinMessage;
import com.guinardpouard.imposteur.application.RoomService;
import com.guinardpouard.imposteur.websocket.dto.RoomUpdatedMessage;
import com.guinardpouard.imposteur.websocket.dto.StartGameMessage;
import com.guinardpouard.imposteur.websocket.mapper.RoomUpdatedMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class GameController {

    private static final Logger log = LoggerFactory.getLogger(GameController.class);

    private final SimpMessagingTemplate template;
    private final RoomService roomService;
    private final GameService gameService;
    private final RoomUpdatedMapper roomUpdatedMapper;

    public GameController(SimpMessagingTemplate template, RoomService roomService,
                          GameService gameService, RoomUpdatedMapper roomUpdatedMapper) {
        this.template = template;
        this.roomService = roomService;
        this.gameService = gameService;
        this.roomUpdatedMapper = roomUpdatedMapper;
    }

    @MessageMapping("/room.create")
    public void create(RoomCreateMessage msg, Principal principal) {
        String hostId = principal.getName();
        Room room = roomService.createRoom(msg.roomName(), hostId);
        RoomUpdatedMessage roomUpdatedMessage = roomUpdatedMapper.toMessage(room);
        log.info("Host {} created room {}", hostId, roomUpdatedMessage.roomId());
        template.convertAndSend(
                "/topic/room",
                roomUpdatedMapper.toMessage(room)
        );
    }

    @MessageMapping("/room.join")
    public void join(RoomJoinMessage msg, Principal principal) {
        String userId = principal.getName();
        Room room = roomService.addPlayerToRoom(userId, msg.roomId(), msg.playerName());
        log.info("User {} joined room {} with pseudo {}", userId, msg.roomId(), msg.playerName());
        template.convertAndSend(
                "/topic/room/" + msg.roomId() + "/players",
                roomUpdatedMapper.toMessage(room)
        );
    }

    @MessageMapping("/room.start-game")
    public void startGame(StartGameMessage msg, Principal principal) {
        Room room = gameService.startGame(msg.roomId(), principal.getName());

    }

}
