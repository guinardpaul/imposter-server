package com.guinardpouard.imposteur.application;

import com.guinardpouard.imposteur.domain.model.Room;
import com.guinardpouard.imposteur.domain.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class GameServiceTests {

    private GameService gameService;
    @Mock
    private RoomRepository mockRoomRepository;

    @BeforeEach
    public void setup() {
        gameService = new GameService(mockRoomRepository);
    }

    @Test
    void createRoom_shouldCreate_a_new_room() {
        Room room = gameService.createRoom();
        assertThat(room).isNotNull();
        assertThat(room.getPlayers()).isEmpty();
        assertThat(room.getRoomId()).isNotNull();
        assertThat(room.getRoomName()).isNotNull();
    }

    @Test
    void addPlayerToRoom_should_add_player_and_return_updated_room() {
        Room room = gameService.createRoom();
        Mockito.when(mockRoomRepository.findById(room.getRoomId())).thenReturn(Optional.of(room));
        room = gameService.addPlayerToRoom(room.getRoomId(), "player1");
        assertThat(room).isNotNull();
        assertThat(room.getPlayers()).hasSize(1);
        assertThat(room.getPlayers().getFirst().getPlayerName()).isEqualTo("player1");
        assertThat(room.getPlayers().getFirst().getPlayerId()).isNotNull();
    }

    @Test
    void addPlayerToRoom_should_throw_Exception_when_room_does_not_exist() {
     assertThatThrownBy(
             () -> gameService.addPlayerToRoom("unknown_RoomId", "player1"))
             .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Room does not exist");
    }

}
