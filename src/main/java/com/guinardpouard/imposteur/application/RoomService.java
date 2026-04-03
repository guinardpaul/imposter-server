package com.guinardpouard.imposteur.application;

import com.guinardpouard.imposteur.domain.model.Player;
import com.guinardpouard.imposteur.domain.model.Room;
import com.guinardpouard.imposteur.application.port.GamePublisher;
import com.guinardpouard.imposteur.application.port.RoomRepository;
import com.guinardpouard.imposteur.application.mapper.RoomUpdatedMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final static Logger log = LoggerFactory.getLogger(RoomService.class);
    private final RoomRepository roomRepository;
    private final GamePublisher gamePublisher;
    private final RoomUpdatedMapper roomUpdatedMapper;

    @Autowired
    public RoomService(RoomRepository roomRepository, GamePublisher gamePublisher,
                       RoomUpdatedMapper roomUpdatedMapper) {
        this.roomRepository = roomRepository;
        this.gamePublisher = gamePublisher;
        this.roomUpdatedMapper = roomUpdatedMapper;
    }

    public void createRoom(String roomName, String hostId) {
        Room room = new Room(roomName, hostId);
        gamePublisher.publishRoomCreated(roomUpdatedMapper.toMessage(room));
        roomRepository.save(room);
        log.info("Room {} with id {} created", room.getRoomName(), room.getRoomId());
    }

    public void addPlayerToRoom(String userId, String roomId, String playerName) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room does not exist"));
        room.join(Player.player(userId, playerName));
        gamePublisher.publishRoomJoined(roomUpdatedMapper.toMessage(room));
        roomRepository.save(room);
        log.info("Player {} with connectionId {} joined room {}", playerName, userId, room.getRoomId());
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

}
