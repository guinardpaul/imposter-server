package com.guinardpouard.imposteur.application;

import com.guinardpouard.imposteur.domain.model.Room;
import com.guinardpouard.imposteur.domain.model.WordPair;
import com.guinardpouard.imposteur.domain.repository.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final RoomRepository roomRepository;

    public GameService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room startGame(String roomId, String hostId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room does not exist"));

        WordPair wordPair = new WordPair("Pomme", "Avion");
        room.startGame(hostId, wordPair);
        roomRepository.save(room);

        return room;
    }

    public Room startNextRound(String roomId, String hostId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room does not exist"));

        WordPair wordPair = new WordPair("Pomme", "Avion");
        room.getCurrentGame().startNextRound(wordPair);
        roomRepository.save(room);

        return room;
    }

}
