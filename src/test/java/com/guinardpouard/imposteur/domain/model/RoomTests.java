package com.guinardpouard.imposteur.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
    void addPlayer_should_add_a_player_to_the_room() {
        Room room = new Room("room2");
        assertThat(room.getPlayers()).isNotNull().hasSize(0);
        room.addPlayer(new Player("player1"));
        assertThat(room.getPlayers()).hasSize(1);
    }

}
