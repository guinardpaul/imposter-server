package com.guinardpouard.imposteur.websocket;

import com.guinardpouard.imposteur.application.GameService;
import com.guinardpouard.imposteur.domain.model.Player;
import com.guinardpouard.imposteur.domain.model.Room;
import com.guinardpouard.imposteur.websocket.dto.PlayerView;
import com.guinardpouard.imposteur.websocket.dto.RoomCreatedMessage;
import com.guinardpouard.imposteur.websocket.dto.RoomJoinMessage;
import com.guinardpouard.imposteur.websocket.dto.RoomUpdatedMessage;
import com.guinardpouard.imposteur.websocket.mapper.RoomCreatedMapper;
import com.guinardpouard.imposteur.websocket.mapper.RoomUpdatedMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameControllerTests {

    @Mock
    private SimpMessagingTemplate messagingTemplate;
    @Mock
    private GameService gameService;
    @Mock
    private RoomUpdatedMapper roomUpdatedMapper;
    @Mock
    private RoomCreatedMapper roomCreatedMapper;
    @InjectMocks
    private GameController controller;

    @Test
    void should_send_room_to_everyone_when_creating_a_room() {
        // given
        Room room = new Room("room1");
        when(gameService.createRoom()).thenReturn(room);
        when(roomCreatedMapper.toMessage(room)).thenCallRealMethod();

        // when
        controller.create();

        // then
        verify(messagingTemplate).convertAndSend(
                eq("/topic/room"),
                eq(new RoomCreatedMessage(room.getRoomId(), room.getRoomName()))
        );
    }

    @Test
    void should_add_player_and_return_all_players_in_the_room_when_joining_a_room() {
        // given a created room
        Room room = new Room("room2");
        room.addPlayer(new Player("player 1"));
        when(gameService.addPlayerToRoom(room.getRoomId(), "player 1")).thenReturn(room);

        List<PlayerView> playerViewList = new ArrayList<>();
        playerViewList.add(new PlayerView(room.getPlayers().getFirst().getPlayerId(), "player 1"));
        when(roomUpdatedMapper.toMessage(room)).thenCallRealMethod();

        // when a player join the room
        controller.join(new RoomJoinMessage(room.getRoomId(), "player 1"));

        // then
        verify(messagingTemplate).convertAndSend(
                eq("/topic/room/" + room.getRoomId() + "/players"),
                eq(new RoomUpdatedMessage(
                        room.getRoomId(),
                        room.getRoomName(),
                        playerViewList))
        );
    }

}
