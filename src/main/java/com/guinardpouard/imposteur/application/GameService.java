package com.guinardpouard.imposteur.application;

import com.guinardpouard.imposteur.application.mapper.RoomUpdatedMapper;
import com.guinardpouard.imposteur.domain.model.*;
import com.guinardpouard.imposteur.application.port.GamePublisher;
import com.guinardpouard.imposteur.domain.repository.RoomRepository;
import com.guinardpouard.imposteur.application.mapper.PrivateRoomUpdateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GameService {

    private final static Logger log = LoggerFactory.getLogger(GameService.class);
    private final RoomRepository roomRepository;
    private final GamePublisher gamePublisher;
    private final PrivateRoomUpdateMapper privateRoomUpdateMapper;
    private final RoomUpdatedMapper roomUpdatedMapper;

    public GameService(RoomRepository roomRepository, GamePublisher gamePublisher,
                       PrivateRoomUpdateMapper privateRoomUpdateMapper, RoomUpdatedMapper roomUpdatedMapper) {
        this.roomRepository = roomRepository;
        this.gamePublisher = gamePublisher;
        this.privateRoomUpdateMapper = privateRoomUpdateMapper;
        this.roomUpdatedMapper = roomUpdatedMapper;
    }

    public void startGame(String roomId, String hostId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room does not exist"));
        log.info("Starting game for room {}", room.getRoomId());

        WordPair wordPair = new WordPair("Pomme", "Avion");
        room.startGame(hostId, wordPair);

        gamePublisher.publishGameStarted(
                roomUpdatedMapper.toMessage(room)
        );

        for (Map.Entry<PlayerId, PlayerRoundState> state : room.getStates().entrySet()) {
            gamePublisher.sendWordToPlayer(
                    privateRoomUpdateMapper.toMessage(
                            room.getRoomId(),
                            state.getKey().getValue(),
                            state.getValue().word()
                    )
            );
        }
        log.info("Game started for room {}", room.getRoomId());
        roomRepository.save(room);
    }

    public void startNextRound(String roomId, String hostId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room does not exist"));

        WordPair wordPair = new WordPair("Pomme", "Avion");
        room.startNextRound(wordPair);

        for (Map.Entry<PlayerId, PlayerRoundState> state: room.getStates().entrySet()) {
            gamePublisher.sendWordToPlayer(
                    privateRoomUpdateMapper.toMessage(
                            room.getRoomId(),
                            state.getKey().getValue(),
                            state.getValue().word()
                    )
            );
        }
        log.info("Starting next round for room {}", room.getRoomId());
        roomRepository.save(room);
    }

}
