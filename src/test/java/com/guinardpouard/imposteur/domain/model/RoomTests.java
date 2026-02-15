package com.guinardpouard.imposteur.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RoomTests {

    @Test
    void should_have_random_id() {
        Room room = new Room("room1");
        assertThat(room.getRoomId()).isNotNull();
        assertThat(room.getRoomName()).isNotNull().isEqualTo("room1");
    }

    @Test
    void should_initialize_a_list_of_players() {
        Room room = new Room("room2");
        assertThat(room.getPlayers()).isNotNull().hasSize(0);
    }

    @Test
    void join_should_add_a_player_to_the_room() {
        Room room = new Room("room2");
        assertThat(room.getPlayers()).isNotNull().hasSize(0);
        room.addPlayer(new Player("player1"));
        assertThat(room.getPlayers()).hasSize(1);
    }

    @Test
    void room_with_not_enough_players_cannot_start() {
        Room room = new Room("room3");
        room.addPlayer(Player.player("user-1", "player1"));
        assertThat(room.getState().state()).isEqualTo(GamePhase.JOINING);
        assertThatThrownBy(room::startGame).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Game cannot start with less than");
    }

    @Test
    void room_with_enough_players_can_start() {
        Room room = new Room("room3");
        room.addPlayer(Player.player("user-1", "player1"));
        room.addPlayer(Player.player("user-2", "player2"));
        assertThat(room.getState().state()).isEqualTo(GamePhase.JOINING);

        room.startGame();

        assertThat(room.getState()).isInstanceOf(InProgressState.class);
        assertThat(room.getState().state()).isEqualTo(GamePhase.IN_PROGRESS);
    }

    @Test
    void room_in_progress_state_should_not_start_or_join_players() {
        Room room = new Room("room3");
        room.addPlayer(Player.player("user-1", "player1"));
        room.addPlayer(Player.player("user-2", "player2"));
        assertThat(room.getState().state()).isEqualTo(GamePhase.JOINING);
        room.startGame();
        assertThat(room.getState()).isInstanceOf(InProgressState.class);
        assertThat(room.getState().state()).isEqualTo(GamePhase.IN_PROGRESS);

        assertThatThrownBy(room::startGame).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Game has already started");
        assertThatThrownBy(() -> room.join(Player.player("user-3", "player3"))).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Game has already started");
    }

    @Test
    void finished_game_cannot_finish_again() {
        Room room = new Room("room3");
        room.addPlayer(Player.player("user-1", "player1"));
        room.addPlayer(Player.player("user-2", "player2"));
        assertThat(room.getState().state()).isEqualTo(GamePhase.JOINING);
        room.startGame();
        assertThat(room.getState()).isInstanceOf(InProgressState.class);
        assertThat(room.getState().state()).isEqualTo(GamePhase.IN_PROGRESS);
        room.endGame();
        assertThat(room.getState()).isInstanceOf(FinishedState.class);
        assertThat(room.getState().state()).isEqualTo(GamePhase.FINISHED);

        assertThatThrownBy(room::endGame).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Game has already ended");

        assertThat(room.getPlayers()).hasSize(2);
        room.join(Player.player("user-2", "player2"));
        assertThat(room.getPlayers()).hasSize(3);

        room.startGame();
        assertThat(room.getState()).isInstanceOf(InProgressState.class);
    }

    @Test
    void joining_room_can_be_finished() {
        Room room = new Room("room3");
        assertThat(room.getState().state()).isEqualTo(GamePhase.JOINING);
        room.endGame();
        assertThat(room.getState()).isInstanceOf(FinishedState.class);
        assertThat(room.getState().state()).isEqualTo(GamePhase.FINISHED);
    }

}
