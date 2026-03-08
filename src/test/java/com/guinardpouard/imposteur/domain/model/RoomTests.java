package com.guinardpouard.imposteur.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RoomTests {

    private WordPair wordPair;

    @BeforeEach
    void setup() {
        wordPair = new WordPair("mot1", "mot2");
    }

    @Test
    void should_have_a_random_id() {
        Room room = new Room("room1", "hostId");

        assertThat(room.getRoomId()).isNotNull();
        assertThat(room.getRoomName()).isNotNull().isEqualTo("room1");
    }

    @Test
    void should_be_initialized_with_a_playerList() {
        Room room = new Room("room2", "hostId");

        assertThat(room.getPlayers()).isNotNull().hasSize(0);
    }

    @Test
    void join_should_add_a_player_to_the_room() {
        Room room = new Room("room2", "hostId");
        assertThat(room.getPlayers()).isNotNull().hasSize(0);

        room.join(Player.player("user-1", "player1"));
        assertThat(room.getPlayers()).hasSize(1);
    }

    @Test
    void room_with_not_enough_players_cannot_startGame() {
        Room room = new Room("room3", "hostId");
        room.addPlayer(Player.player("user-1", "player1"));

        assertThat(room.getState().state()).isEqualTo(GamePhase.JOINING);

        assertThatThrownBy(() -> room.startGame("hostId", wordPair)).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Game cannot start with less than");
    }

    @Test
    void only_room_host_can_startGame() {
        Room room = new Room("room3", "hostId");
        room.addPlayer(Player.player("user-1", "player1"));
        room.addPlayer(Player.player("user-2", "player2"));
        room.addPlayer(Player.player("user-3", "player3"));

        assertThat(room.getState().state()).isEqualTo(GamePhase.JOINING);

        assertThatThrownBy(() -> room.startGame("user-1", wordPair)).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Game can be started only by host");
    }

    @Test
    void room_with_enough_players_can_start() {
        Room room = new Room("room3", "hostId");
        room.addPlayer(Player.player("user-1", "player1"));
        room.addPlayer(Player.player("user-2", "player2"));
        room.addPlayer(Player.player("user-3", "player3"));
        assertThat(room.getState().state()).isEqualTo(GamePhase.JOINING);

        room.startGame("hostId", wordPair);

        assertThat(room.getState()).isInstanceOf(InProgressState.class);
        assertThat(room.getState().state()).isEqualTo(GamePhase.IN_PROGRESS);
        assertThat(room.getCurrentGame()).isNotNull();
    }

    @Test
    void room_in_progress_state_should_not_start_or_join_players() {
        Room room = new Room("room3", "hostId");
        room.addPlayer(Player.player("user-1", "player1"));
        room.addPlayer(Player.player("user-2", "player2"));
        room.addPlayer(Player.player("user-3", "player3"));
        assertThat(room.getState().state()).isEqualTo(GamePhase.JOINING);
        room.startGame("hostId", wordPair);
        assertThat(room.getState()).isInstanceOf(InProgressState.class);
        assertThat(room.getState().state()).isEqualTo(GamePhase.IN_PROGRESS);

        assertThatThrownBy(() -> room.startGame("hostId", wordPair)).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Game has already started");
        assertThatThrownBy(() -> room.join(Player.player("user-3", "player3"))).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Game has already started");
    }

    @Test
    void finished_game_cannot_finish_again() {
        Room room = new Room("room3", "hostId");
        room.addPlayer(Player.player("user-1", "player1"));
        room.addPlayer(Player.player("user-2", "player2"));
        room.addPlayer(Player.player("user-3", "player3"));
        assertThat(room.getState().state()).isEqualTo(GamePhase.JOINING);
        room.startGame("hostId", wordPair);
        assertThat(room.getState()).isInstanceOf(InProgressState.class);
        assertThat(room.getState().state()).isEqualTo(GamePhase.IN_PROGRESS);
        room.endGame();
        assertThat(room.getState()).isInstanceOf(FinishedState.class);
        assertThat(room.getState().state()).isEqualTo(GamePhase.FINISHED);

        assertThatThrownBy(room::endGame).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Game has already ended");

        assertThat(room.getPlayers()).hasSize(3);
        room.join(Player.player("user-4", "player4"));
        assertThat(room.getPlayers()).hasSize(4);

        room.startGame("hostId", wordPair);
        assertThat(room.getState()).isInstanceOf(InProgressState.class);
    }

    @Test
    void joining_room_can_be_finished() {
        Room room = new Room("room3", "hostId");
        assertThat(room.getState().state()).isEqualTo(GamePhase.JOINING);
        room.endGame();
        assertThat(room.getState()).isInstanceOf(FinishedState.class);
        assertThat(room.getState().state()).isEqualTo(GamePhase.FINISHED);
    }

}
