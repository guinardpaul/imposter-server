package com.guinardpouard.imposteur.application;

import com.guinardpouard.imposteur.domain.model.Player;
import com.guinardpouard.imposteur.domain.model.Room;
import com.guinardpouard.imposteur.domain.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room createRoom(String userId, String roomName, String hostName) {
        Room room = new Room(roomName);
        room.join(Player.host(userId, hostName));
        roomRepository.save(room);
        return room;
    }

    public Room addPlayerToRoom(String userId, String roomId, String playerName) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room does not exist"));
        room.join(Player.player(userId, playerName));
        roomRepository.save(room);

        return room;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

}
