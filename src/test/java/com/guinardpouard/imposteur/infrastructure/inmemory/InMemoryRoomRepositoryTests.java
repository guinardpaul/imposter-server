package com.guinardpouard.imposteur.infrastructure.inmemory;

import com.guinardpouard.imposteur.domain.model.Room;
import com.guinardpouard.imposteur.domain.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryRoomRepositoryTests {

    private RoomRepository roomRepository;

    @BeforeEach
    void setUp() {
        roomRepository = new InMemoryRoomRepository();
    }

    @Test
    void createRoom_should_add_a_room_to_in_memory_data() {
        Room room = new Room("room1");
        roomRepository.save(room);

        Optional<Room> inMemory = roomRepository.findById(room.getRoomId());
        assertThat(inMemory).isPresent();
        assertThat(inMemory.get()).isEqualTo(room);
    }

    @Test
    void findRoomById_should_return_optional_empty_when_room_does_not_exist() {
        Room room = new Room("room1");
        roomRepository.save(room);

        Optional<Room> inMemory = roomRepository.findById("unknown_room_id");
        assertThat(inMemory).isNotPresent();
    }

    @Test
    void deleteRoom_should_delete_a_room_to_in_memory_data() {
        Room room = new Room("room1");
        roomRepository.save(room);
        Optional<Room> inMemoryAfterSave = roomRepository.findById(room.getRoomId());
        assertThat(inMemoryAfterSave).isPresent();

        roomRepository.delete(room.getRoomId());
        Optional<Room> inMemory = roomRepository.findById(room.getRoomId());
        assertThat(inMemory).isNotPresent();
    }

    @Test
    void findAll_should_return_empty_list_when_no_rooms_exists() {
        List<Room> rooms = roomRepository.findAll();
        assertThat(rooms).isNotNull();
        assertThat(rooms).isEmpty();
    }

    @Test
    void findAll_should_return_existing_room() {
        roomRepository.save(new Room("room1"));
        roomRepository.save(new Room("room2"));

        List<Room> rooms = roomRepository.findAll();
        assertThat(rooms).isNotNull();
        assertThat(rooms).hasSize(2);
    }

}
