package com.guinardpouard.imposteur.application;

import com.guinardpouard.imposteur.domain.model.GameSession;
import com.guinardpouard.imposteur.domain.model.Player;
import com.guinardpouard.imposteur.domain.model.Room;
import com.guinardpouard.imposteur.domain.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTests {

    private GameService gameService;
    @Mock
    private RoomRepository mockRoomRepository;

    @BeforeEach
    void setup() {
        gameService = new GameService(mockRoomRepository);
    }

    @Test
    void starting_the_game_should_start_a_gameSession_and_a_round() {
        Room room = new Room("room 1", "hostConnectionId");
        room.join(Player.player("p1"));
        room.join(Player.player("p2"));
        room.join(Player.player("p3"));
        when(mockRoomRepository.findById(room.getRoomId())).thenReturn(Optional.of(room));

        Room res = gameService.startGame(room.getRoomId(), "hostConnectionId");

        assertThat(res.getCurrentGame()).isNotNull();
        assertThat(res.getPlayers()).hasSize(3);
        GameSession currentGame = res.getCurrentGame();
        assertThat(currentGame.getCurrentRound()).isNotNull();
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
                .hasMessage("Game not started yet");
    }

    @Test
    void should_start_a_new_round_on_a_started_game() {
        Room room = new Room("room 1", "hostConnectionId");
        room.join(Player.player("p1"));
        room.join(Player.player("p2"));
        room.join(Player.player("p3"));
        when(mockRoomRepository.findById(room.getRoomId())).thenReturn(Optional.of(room));
        room = gameService.startGame(room.getRoomId(), "hostConnectionId");
        assertThat(room.getCurrentGame()).isNotNull();

        Room res = gameService.startNextRound(room.getRoomId(), "hostConnectionId");

        assertThat(res.getPlayers()).hasSize(3);
        GameSession currentGame = res.getCurrentGame();
        assertThat(currentGame.getCurrentRound()).isNotNull();
    }
}
