package com.guinardpouard.imposteur.application;

import com.guinardpouard.imposteur.domain.model.Player;
import com.guinardpouard.imposteur.domain.model.Room;
import com.guinardpouard.imposteur.application.port.GamePublisher;
import com.guinardpouard.imposteur.domain.repository.RoomRepository;
import com.guinardpouard.imposteur.application.mapper.PrivateRoomUpdateMapper;
import com.guinardpouard.imposteur.application.event.PrivateRoomUpdatedMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTests {

    private GameService gameService;
    @Mock
    private RoomRepository mockRoomRepository;
    @Mock
    private GamePublisher mockGamePublisher;
    @Mock
    private PrivateRoomUpdateMapper mockMapper;

    @BeforeEach
    void setup() {
        gameService = new GameService(mockRoomRepository, mockGamePublisher, mockMapper);
    }

    @Test
    void starting_the_game_should_start_a_gameSession_and_a_round() {
        Room room = new Room("room 1", "hostConnectionId");
        room.join(Player.player("p1"));
        room.join(Player.player("p2"));
        room.join(Player.player("p3"));
        when(mockRoomRepository.findById(room.getRoomId())).thenReturn(Optional.of(room));
        when(mockMapper.toMessage(any(), any(), any())).thenCallRealMethod();

        gameService.startGame(room.getRoomId(), "hostConnectionId");


        verify(mockRoomRepository, times(1))
                .findById(eq(room.getRoomId()));
        verify(mockGamePublisher, times(3))
                .sendWordToPlayer(any(PrivateRoomUpdatedMessage.class)
                );
        verify(mockRoomRepository, times(1))
                .save(eq(room));
    }

    @Test
    void should_throw_exception_if_game_not_started_and_trying_to_create_next_round() {
        Room room = new Room("room 1", "hostConnectionId");
        room.join(Player.player("p1"));
        room.join(Player.player("p2"));
        room.join(Player.player("p3"));
        when(mockRoomRepository.findById(room.getRoomId())).thenReturn(Optional.of(room));

        assertThatThrownBy(() -> gameService.startNextRound(room.getRoomId(), "hostConnectionId"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Game not started");
    }

    @Test
    void should_start_a_new_round_on_a_started_game() {
        Room room = new Room("room 1", "hostConnectionId");
        room.join(Player.player("p1"));
        room.join(Player.player("p2"));
        room.join(Player.player("p3"));
        when(mockRoomRepository.findById(room.getRoomId())).thenReturn(Optional.of(room));
        when(mockMapper.toMessage(any(), any(), any())).thenCallRealMethod();
        gameService.startGame(room.getRoomId(), "hostConnectionId");

        gameService.startNextRound(room.getRoomId(), "hostConnectionId");

        // Then
        verify(mockRoomRepository, times(2))
                .findById(eq(room.getRoomId()));
        verify(mockGamePublisher, times(6))
                .sendWordToPlayer(any(PrivateRoomUpdatedMessage.class)
                );
        verify(mockRoomRepository, times(2))
                .save(eq(room));
    }
}
