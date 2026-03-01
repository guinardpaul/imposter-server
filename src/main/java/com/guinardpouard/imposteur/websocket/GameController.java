package com.guinardpouard.imposteur.websocket;

import com.guinardpouard.imposteur.domain.model.Room;
import com.guinardpouard.imposteur.websocket.dto.RoomJoinMessage;
import com.guinardpouard.imposteur.application.RoomService;
import com.guinardpouard.imposteur.websocket.dto.RoomUpdatedMessage;
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
    private final RoomUpdatedMapper roomUpdatedMapper;
    private final RoomCreatedMapper roomCreatedMapper;

    public GameController(SimpMessagingTemplate template, RoomService roomService, RoomUpdatedMapper roomUpdatedMapper) {
        this.template = template;
        this.roomService = roomService;
        this.roomUpdatedMapper = roomUpdatedMapper;
        this.roomCreatedMapper = roomCreatedMapper;
    }

    @MessageMapping("/room.create")
    public void create(RoomCreateMessage msg, Principal principal) {
        String userId = principal.getName();
        Room room = gameService.createRoom(userId, msg.roomName(), msg.playerName());
        RoomUpdatedMessage roomUpdatedMessage = roomUpdatedMapper.toMessage(room);
        log.info("User {} created room {}", userId, roomUpdatedMessage.roomId());
        template.convertAndSend(
                "/topic/room",
                roomUpdatedMapper.toMessage(room)
        );
    }

    @MessageMapping("/room.join")
    public void join(RoomJoinMessage msg, Principal principal) {
        String userId = principal.getName();
        Room room = gameService.addPlayerToRoom(userId, msg.roomId(), msg.playerName());
        log.info("User {} joined room {} with pseudo {}", userId, msg.roomId(), msg.playerName());
        template.convertAndSend(
                "/topic/room/" + msg.roomId() + "/players",
                roomUpdatedMapper.toMessage(room)
        );
    }

    @MessageMapping("/room.clear")
    public void clearAllRooms() {
        roomService.clearAllRooms();
        template.convertAndSend(
                "/topic/room/",
                roomService
                        .getAllRooms()
                        .stream()
                        .map(roomUpdatedMapper::toMessage)
                        .toList()
        );
    }

}
