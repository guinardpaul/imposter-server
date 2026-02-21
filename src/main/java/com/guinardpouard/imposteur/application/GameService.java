package com.guinardpouard.imposteur.application;

import com.guinardpouard.imposteur.domain.model.Player;
import com.guinardpouard.imposteur.domain.model.Room;
import com.guinardpouard.imposteur.domain.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class GameService {

    private final AtomicLong atomicId = new AtomicLong();
    private final RoomRepository roomRepository;

    @Autowired
    public GameService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room createRoom() {
        Room room = new Room("Room_" + atomicId.incrementAndGet());
        roomRepository.save(room);
        return room;
    }

    public Room addPlayerToRoom(String roomId, String playerName) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room does not exist"));
        room.addPlayer(new Player(playerName));
        roomRepository.save(room);

        return room;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

}
