package com.guinardpouard.imposteur.application;

import com.guinardpouard.imposteur.domain.model.Room;
import com.guinardpouard.imposteur.domain.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTests {

    private RoomService roomService;
    @Mock
    private RoomRepository mockRoomRepository;

    @BeforeEach
    public void setup() {
        roomService = new RoomService(mockRoomRepository);
    }

    @Test
    void createRoom_shouldCreate_a_new_room() {
        Room room = roomService.createRoom("room 123", "user-123");
        assertThat(room).isNotNull();
        assertThat(room.getPlayers()).isEmpty();
        assertThat(room.getRoomId()).isNotNull();
        assertThat(room.getRoomName()).isEqualTo("room 123");
    }

    @Test
    void addPlayerToRoom_should_add_player_and_return_updated_room() {
        Room room = new Room("room 1", "user-123");
        when(mockRoomRepository.findById(room.getRoomId())).thenReturn(Optional.of(room));

        room = roomService.addPlayerToRoom("user-456", room.getRoomId(), "player2");
        assertThat(room).isNotNull();
        assertThat(room.getPlayers()).hasSize(1);
        assertThat(room.getPlayers().getFirst().getName()).isEqualTo("player2");
        assertThat(room.getPlayers().getFirst().getConnectionId()).isEqualTo("user-456");
        assertThat(room.getPlayers().getFirst().id()).isNotNull();
    }

    @Test
    void addPlayerToRoom_should_throw_Exception_when_room_does_not_exist() {
        assertThatThrownBy(
                () -> roomService.addPlayerToRoom("user-123", "unknown_RoomId", "player1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Room does not exist");
    }

    @Test
    void getAllRooms_should_return_an_empty_list_when_no_rooms_exists() {
        when(mockRoomRepository.findAll()).thenReturn(List.of());
        assertThat(roomService.getAllRooms()).isNotNull();
        assertThat(roomService.getAllRooms()).isEmpty();
    }

    @Test
    void getAllRooms_should_return_existing_rooms() {
        when(mockRoomRepository.findAll())
                .thenReturn(
                        List.of(
                                new Room("room1","host1"),
                                new Room("room2","host2")
                        )
                );
        List<Room> rooms = roomService.getAllRooms();
        assertThat(rooms).isNotNull();
        assertThat(rooms).hasSize(2);
    }

}
