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

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
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
        Player p1 = Player.host("user1", "player1");
        room.join(p1);
        when(gameService.createRoom(anyString(), anyString(), anyString())).thenReturn(room);
        when(roomUpdatedMapper.toMessage(room)).thenCallRealMethod();
        Principal principal = () -> "user-123";
        RoomCreateMessage msg = new RoomCreateMessage("room 123", "player 456");
        // when
        controller.create(msg, principal);

        List<PlayerView> list = new ArrayList<>();
        list.add(new PlayerView("user1", "player1"));

        // then
        verify(messagingTemplate).convertAndSend(
                eq("/topic/room"),
                eq(new RoomUpdatedMessage(room.getRoomId(), room.getRoomName(), list))
        );
    }

    @Test
    void should_add_player_and_return_all_players_in_the_room_when_joining_a_room() {
        // given a created room
        Room room = new Room("room2");
        room.join(Player.player("user1", "player 1"));
        when(gameService.addPlayerToRoom("user-2", room.getRoomId(), "player 1")).thenReturn(room);

        List<PlayerView> playerViewList = new ArrayList<>();
        playerViewList.add(new PlayerView(room.getPlayers().getFirst().getUserId(), "player 1"));
        when(roomUpdatedMapper.toMessage(room)).thenCallRealMethod();
        Principal principal = () -> "user-2";

        // when a player join the room
        controller.join(new RoomJoinMessage(room.getRoomId(), "player 1"), principal);

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
