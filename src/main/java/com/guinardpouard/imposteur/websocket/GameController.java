package com.guinardpouard.imposteur.websocket;

import com.guinardpouard.imposteur.domain.model.Player;
import com.guinardpouard.imposteur.domain.model.Room;
import com.guinardpouard.imposteur.websocket.dto.RoomCreateMessage;
import com.guinardpouard.imposteur.websocket.dto.PrivateRoomUpdatedMessage;
import com.guinardpouard.imposteur.websocket.dto.RoomJoinMessage;
import com.guinardpouard.imposteur.application.RoomService;
import com.guinardpouard.imposteur.websocket.dto.RoomUpdatedMessage;
import com.guinardpouard.imposteur.application.GameService;
import com.guinardpouard.imposteur.websocket.dto.StartGameMessage;
import com.guinardpouard.imposteur.websocket.mapper.GameStartedMapper;
import com.guinardpouard.imposteur.websocket.mapper.RoomCreatedMapper;
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
    private final GameStartedMapper gameStartedMapper;

    public GameController(SimpMessagingTemplate template, RoomService roomService, RoomUpdatedMapper roomUpdatedMapper) {
    public GameController(SimpMessagingTemplate template, GameService gameService, RoomUpdatedMapper roomUpdatedMapper,
                          RoomCreatedMapper roomCreatedMapper, GameStartedMapper gameStartedMapper) {
        this.template = template;
        this.roomService = roomService;
        this.roomUpdatedMapper = roomUpdatedMapper;
        this.roomCreatedMapper = roomCreatedMapper;
        this.gameStartedMapper = gameStartedMapper;
    }

    @MessageMapping("/room.create")
    public void create(RoomCreateMessage msg, Principal principal) {
        String userId = principal.getName();
        Room room = roomService.createRoom(userId, msg.roomName(), msg.playerName());
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
        Room room = roomService.addPlayerToRoom(userId, msg.roomId(), msg.playerName());
        log.info("User {} joined room {} with pseudo {}", userId, msg.roomId(), msg.playerName());
        template.convertAndSend(
                "/topic/room/" + msg.roomId() + "/players",
                roomUpdatedMapper.toMessage(room)
        );
    }

    @MessageMapping("/room.start-game")
    public void startGame(StartGameMessage msg) {
        Room room = gameService.startGame(msg.roomId());
        for (Player p : room.getPlayers()) {
            template.convertAndSendToUser(
                    "userid",
                    "/queue/room/" + msg.roomId(),
                    gameStartedMapper.toMessage(room));
        }
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
