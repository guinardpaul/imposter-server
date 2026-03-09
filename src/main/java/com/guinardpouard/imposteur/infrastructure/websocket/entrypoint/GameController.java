package com.guinardpouard.imposteur.infrastructure.websocket.entrypoint;

import com.guinardpouard.imposteur.application.GameService;
import com.guinardpouard.imposteur.infrastructure.websocket.entrypoint.dto.CreateRoomMessage;
import com.guinardpouard.imposteur.infrastructure.websocket.entrypoint.dto.JoinRoomMessage;
import com.guinardpouard.imposteur.application.RoomService;
import com.guinardpouard.imposteur.infrastructure.websocket.entrypoint.dto.StartGameMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class GameController {

    private final RoomService roomService;
    private final GameService gameService;

    public GameController(RoomService roomService, GameService gameService) {
        this.roomService = roomService;
        this.gameService = gameService;
    }

    @MessageMapping("/room.create")
    public void create(CreateRoomMessage msg, Principal principal) {
        roomService.createRoom(msg.roomName(), principal.getName());
    }

    @MessageMapping("/room.join")
    public void join(JoinRoomMessage msg, Principal principal) {
        roomService.addPlayerToRoom(principal.getName(), msg.roomId(), msg.playerName());
    }

    @MessageMapping("/room.start")
    public void startGame(StartGameMessage msg, Principal principal) {
        gameService.startGame(msg.roomId(), principal.getName());
    }


}
