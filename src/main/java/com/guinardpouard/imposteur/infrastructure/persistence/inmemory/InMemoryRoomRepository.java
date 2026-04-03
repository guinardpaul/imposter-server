package com.guinardpouard.imposteur.infrastructure.persistence.inmemory;

import com.guinardpouard.imposteur.domain.model.Room;
import com.guinardpouard.imposteur.application.port.RoomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryRoomRepository implements RoomRepository {

    private final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();

    @Override
    public Optional<Room> findById(String roomId) {
        if (rooms.containsKey(roomId)) {
            return Optional.of(rooms.get(roomId));
        }
        return Optional.empty();
    }

    @Override
    public void save(Room room) {
        rooms.put(room.getRoomId(), room);
    }

    @Override
    public void delete(String roomId) {
        rooms.remove(roomId);
    }

    @Override
    public List<Room> findAll() {
        return rooms.values().stream().toList();
    }

}
