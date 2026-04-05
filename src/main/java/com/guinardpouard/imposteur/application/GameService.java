package com.guinardpouard.imposteur.application;

import com.guinardpouard.imposteur.application.mapper.RoomUpdatedMapper;
import com.guinardpouard.imposteur.domain.model.*;
import com.guinardpouard.imposteur.application.port.GamePublisher;
import com.guinardpouard.imposteur.application.port.RoomRepository;
import com.guinardpouard.imposteur.application.mapper.PrivateRoomUpdateMapper;
import com.guinardpouard.imposteur.domain.port.ApiWordProvider;
import com.guinardpouard.imposteur.domain.service.WordSelector;
import com.guinardpouard.imposteur.infrastructure.persistence.JsonWordProvider;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.MappedInterceptor;

import java.util.Map;

@Service
public class GameService {

    private final static Logger log = LoggerFactory.getLogger(GameService.class);
    private final RoomRepository roomRepository;
    private final GamePublisher gamePublisher;
    private final PrivateRoomUpdateMapper privateRoomUpdateMapper;
    private final RoomUpdatedMapper roomUpdatedMapper;
    private final WordSelector wordSelector;

    public GameService(RoomRepository roomRepository, GamePublisher gamePublisher,
                       PrivateRoomUpdateMapper privateRoomUpdateMapper, RoomUpdatedMapper roomUpdatedMapper,
                       ApiWordProvider apiWordProvider) {
        this.roomRepository = roomRepository;
        this.gamePublisher = gamePublisher;
        this.privateRoomUpdateMapper = privateRoomUpdateMapper;
        this.roomUpdatedMapper = roomUpdatedMapper;
        this.wordSelector = new WordSelector(apiWordProvider);
    }

    public void startGame(String roomId, String hostId) {
        Room room = getRoomByRoomId(roomId);
        log.info("Starting game for room {}", room.getRoomId());

        WordPair wordPair = getWordPair(room);
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
        Room room = getRoomByRoomId(roomId);
        log.info("Starting next round for room {}", room.getRoomId());

        WordPair wordPair = getWordPair(room);
        room.startNextRound(hostId, wordPair);

        for (Map.Entry<PlayerId, PlayerRoundState> state : room.getStates().entrySet()) {
            gamePublisher.sendWordToPlayer(
                    privateRoomUpdateMapper.toMessage(
                            room.getRoomId(),
                            state.getKey().getValue(),
                            state.getValue().word()
                    )
            );
        }
        log.info("Next round started for room {}", room.getRoomId());
        roomRepository.save(room);
    }

    private @NonNull Room getRoomByRoomId(String roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room does not exist"));
    }

    private @NonNull WordPair getWordPair(Room room) {
        return wordSelector.select(room.getCurrentGame());
    }

}
