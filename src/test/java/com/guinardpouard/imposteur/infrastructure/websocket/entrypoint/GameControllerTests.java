package com.guinardpouard.imposteur.infrastructure.websocket.entrypoint;

import com.guinardpouard.imposteur.application.GameService;
import com.guinardpouard.imposteur.application.RoomService;
import com.guinardpouard.imposteur.infrastructure.websocket.entrypoint.dto.CreateRoomMessage;
import com.guinardpouard.imposteur.infrastructure.websocket.entrypoint.dto.JoinRoomMessage;
import com.guinardpouard.imposteur.infrastructure.websocket.entrypoint.dto.UpdateGameMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GameControllerTests {

    @Mock
    private RoomService roomService;
    @Mock
    private GameService gameService;
    @InjectMocks
    private GameController controller;

    @Test
    void should_send_room_to_everyone_when_creating_a_room() {
        // given
        Principal principal = () -> "user-123";
        CreateRoomMessage msg = new CreateRoomMessage("room 123");
        // when
        controller.create(msg, principal);
        // then
        verify(roomService).createRoom(
                eq("room 123"),
                eq("user-123")
        );
    }

    @Test
    void should_add_player_and_return_all_players_in_the_room_when_joining_a_room() {
        // given
        Principal principal = () -> "user-2";
        // when a player join the room
        controller.join(new JoinRoomMessage("room-id", "player 1"), principal);
        // then
        verify(roomService).addPlayerToRoom(
                eq("user-2"),
                eq("room-id"),
                eq("player 1")
        );
    }

    @Test
    void should_start_game() {
        Principal principal = () -> "12345";
        UpdateGameMessage msg = new UpdateGameMessage("roomId", "12345");

        controller.startGame(msg, principal);

        verify(gameService).startGame(
                eq("roomId"),
                eq("12345")
        );
    }

    @Test
    void should_start_next_round() {
        Principal principal = () -> "12345";
        UpdateGameMessage msg = new UpdateGameMessage("roomId", "12345");

        controller.startNextRound(msg, principal);

        verify(gameService).startNextRound(
                eq("roomId"),
                eq("12345")
        );
    }

}
