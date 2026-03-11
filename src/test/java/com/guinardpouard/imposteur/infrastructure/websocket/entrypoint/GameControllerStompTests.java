package com.guinardpouard.imposteur.infrastructure.websocket.entrypoint;

import com.guinardpouard.imposteur.application.event.PrivateRoomUpdatedMessage;
import com.guinardpouard.imposteur.infrastructure.websocket.entrypoint.dto.CreateRoomMessage;
import com.guinardpouard.imposteur.infrastructure.websocket.entrypoint.dto.JoinRoomMessage;
import com.guinardpouard.imposteur.application.event.RoomUpdatedMessage;
import com.guinardpouard.imposteur.infrastructure.websocket.entrypoint.dto.StartGameMessage;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.JacksonJsonMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameControllerStompTests {

    @LocalServerPort
    private int port;

    private WebSocketStompClient stompClient;
    private String websocketUrl;

    @BeforeEach
    void setup() {
        websocketUrl = "ws://localhost:" + port + "/websocket";
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new JacksonJsonMessageConverter());
    }

    @Test
    void connection_should_fail_if_header_playerId_is_missing() {
        assertThatThrownBy(() -> stompClient
                .connectAsync(websocketUrl, new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS))
                .isInstanceOf(Exception.class);
    }

    @Test
    void disconnect_should_disconnect_existing_ws_session() throws ExecutionException, InterruptedException, TimeoutException {
        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("player-id", "host-connection-id");
        StompSession session = stompClient
                .connectAsync(websocketUrl, new WebSocketHttpHeaders(), connectHeaders, new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        session.disconnect();
        assertThat(session.isConnected()).isFalse();
    }

    @Test
    void whenPlayerCreatesNewRoom_shouldReceiveRoomCreatedMessage() throws Exception {
        BlockingQueue<RoomUpdatedMessage> blockingQueue = new LinkedBlockingDeque<>();

        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("player-id", "host-connection-id");
        StompSession session = stompClient
                .connectAsync(websocketUrl, new WebSocketHttpHeaders(), connectHeaders, new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        session.subscribe("/topic/room", new StompFrameHandler() {
            @Override
            public Type getPayloadType(@NonNull StompHeaders headers) {
                return RoomUpdatedMessage.class;
            }

            @Override
            public void handleFrame(@NonNull StompHeaders headers, Object payload) {
                blockingQueue.add((RoomUpdatedMessage) payload);
            }
        });

        CreateRoomMessage createRoomMessage = new CreateRoomMessage("room 1");

        session.send("/app/room.create", createRoomMessage);

        RoomUpdatedMessage message = blockingQueue.poll(2, TimeUnit.SECONDS);
        assertThat(message).isNotNull();
        assertThat(message.roomId()).isNotNull();
        assertThat(message.roomName()).isNotNull();
        assertThat(message.playerViewList()).isEmpty();
    }

    @Test
    void whenPlayerJoinsARoom_shouldJoinAndReceiveRoomUpdatedMessage() throws Exception {
        BlockingQueue<RoomUpdatedMessage> createdMessagesBlockingQueue = new LinkedBlockingDeque<>();
        BlockingQueue<RoomUpdatedMessage> updatedMessagesBlockingQueue = new LinkedBlockingDeque<>();

        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("player-id", "host-connection-id");
        StompSession session = stompClient
                .connectAsync(websocketUrl, new WebSocketHttpHeaders(), connectHeaders, new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        // Create room
        session.subscribe("/topic/room", new StompFrameHandler() {
            @Override
            public Type getPayloadType(@NonNull StompHeaders headers) {
                return RoomUpdatedMessage.class;
            }

            @Override
            public void handleFrame(@NonNull StompHeaders headers, Object payload) {
                createdMessagesBlockingQueue.add((RoomUpdatedMessage) payload);
            }
        });
        CreateRoomMessage createRoomMessage = new CreateRoomMessage("room 1");
        session.send("/app/room.create", createRoomMessage);
        RoomUpdatedMessage message = createdMessagesBlockingQueue.poll(2, TimeUnit.SECONDS);
        assertThat(message).isNotNull();
        String roomId = message.roomId();

        session.subscribe("/topic/room/" + roomId + "/**", new StompFrameHandler() {
            @Override
            public Type getPayloadType(@Nonnull StompHeaders headers) {
                return RoomUpdatedMessage.class;
            }

            @Override
            public void handleFrame(@NonNull StompHeaders headers, Object payload) {
                updatedMessagesBlockingQueue.add((RoomUpdatedMessage) payload);
            }
        });

        session.send("/app/room.join", new JoinRoomMessage(roomId, "player 2"));

        RoomUpdatedMessage updatedMessage = updatedMessagesBlockingQueue.poll(2, TimeUnit.SECONDS);
        assertThat(updatedMessage).isNotNull();
        assertThat(updatedMessage.roomId()).isNotNull();
        assertThat(updatedMessage.roomName()).isNotNull();
        assertThat(updatedMessage.playerViewList()).hasSize(1);
    }

    @Test
    void when_host_start_game_should_send_update_on_game_status_and_word_to_all_players() throws ExecutionException, InterruptedException, TimeoutException {
        // Stomp session
        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("player-id", "host-connection-id");
        StompSession session = stompClient
                .connectAsync(websocketUrl, new WebSocketHttpHeaders(), connectHeaders, new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        // Subscribe on update room/game
        BlockingQueue<RoomUpdatedMessage> updatedMessagesBlockingQueue = new LinkedBlockingDeque<>();
        session.subscribe("/topic/room/**", new StompFrameHandler() {
            @Override
            public Type getPayloadType(@Nonnull StompHeaders headers) {
                return RoomUpdatedMessage.class;
            }

            @Override
            public void handleFrame(@NonNull StompHeaders headers, Object payload) {
                updatedMessagesBlockingQueue.add((RoomUpdatedMessage) payload);
            }
        });
        // Given
        // A created room
        session.send("/app/room.create", new CreateRoomMessage("My room"));
        RoomUpdatedMessage startGameMessage = updatedMessagesBlockingQueue.poll(2, TimeUnit.SECONDS);
        assertThat(startGameMessage).isNotNull();
        assertThat(startGameMessage.roomName()).isEqualTo("My room");
        assertThat(startGameMessage.roomId()).isNotNull();
        final String roomId = startGameMessage.roomId();

        // With joined players
        StompHeaders connectHeaders1 = new StompHeaders();
        connectHeaders1.add("player-id", "player-id-1");
        StompSession session1 = stompClient
                .connectAsync(websocketUrl, new WebSocketHttpHeaders(), connectHeaders1, new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);
        session1.send("/app/room.join", new JoinRoomMessage(roomId, "Player 1"));
        RoomUpdatedMessage joinedGameMessage = updatedMessagesBlockingQueue.poll(2, TimeUnit.SECONDS);
        assertPlayerJoinedRoom(joinedGameMessage, 1, roomId);

        StompHeaders connectHeaders2 = new StompHeaders();
        connectHeaders2.add("player-id", "player-id-2");
        StompSession session2 = stompClient
                .connectAsync(websocketUrl, new WebSocketHttpHeaders(), connectHeaders2, new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);
        session2.send("/app/room.join", new JoinRoomMessage(roomId, "Player2"));
        joinedGameMessage = updatedMessagesBlockingQueue.poll(2, TimeUnit.SECONDS);
        assertPlayerJoinedRoom(joinedGameMessage, 2, roomId);

        StompHeaders connectHeaders3 = new StompHeaders();
        connectHeaders3.add("player-id", "player-id-3");
        StompSession session3 = stompClient
                .connectAsync(websocketUrl, new WebSocketHttpHeaders(), connectHeaders3, new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);
        session3.send("/app/room.join", new JoinRoomMessage(roomId, "Player 3"));
        joinedGameMessage = updatedMessagesBlockingQueue.poll(2, TimeUnit.SECONDS);
        assertPlayerJoinedRoom(joinedGameMessage, 3, roomId);

        StompHeaders connectHeaders4 = new StompHeaders();
        connectHeaders4.add("player-id", "player-id-4");
        StompSession session4 = stompClient
                .connectAsync(websocketUrl, new WebSocketHttpHeaders(), connectHeaders4, new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);
        session4.send("/app/room.join", new JoinRoomMessage(roomId, "Player 4"));
        joinedGameMessage = updatedMessagesBlockingQueue.poll(2, TimeUnit.SECONDS);
        assertPlayerJoinedRoom(joinedGameMessage, 4, roomId);

        // game start
        BlockingQueue<PrivateRoomUpdatedMessage> privateUpdatedMessagesBlockingQueue1 = new LinkedBlockingDeque<>();
        session1.subscribe("/user/queue/word", new StompFrameHandler() {
            @Override
            public Type getPayloadType(@Nonnull StompHeaders headers) {
                return PrivateRoomUpdatedMessage.class;
            }

            @Override
            public void handleFrame(@NonNull StompHeaders headers, Object payload) {
                privateUpdatedMessagesBlockingQueue1.add((PrivateRoomUpdatedMessage) payload);
            }
        });

        BlockingQueue<PrivateRoomUpdatedMessage> privateUpdatedMessagesBlockingQueue2 = new LinkedBlockingDeque<>();
        session2.subscribe("/user/queue/word", new StompFrameHandler() {
            @Override
            public Type getPayloadType(@Nonnull StompHeaders headers) {
                return PrivateRoomUpdatedMessage.class;
            }

            @Override
            public void handleFrame(@NonNull StompHeaders headers, Object payload) {
                privateUpdatedMessagesBlockingQueue2.add((PrivateRoomUpdatedMessage) payload);
            }
        });

        BlockingQueue<PrivateRoomUpdatedMessage> privateUpdatedMessagesBlockingQueue3 = new LinkedBlockingDeque<>();
        session3.subscribe("/user/queue/word", new StompFrameHandler() {
            @Override
            public Type getPayloadType(@Nonnull StompHeaders headers) {
                return PrivateRoomUpdatedMessage.class;
            }

            @Override
            public void handleFrame(@NonNull StompHeaders headers, Object payload) {
                privateUpdatedMessagesBlockingQueue3.add((PrivateRoomUpdatedMessage) payload);
            }
        });

        BlockingQueue<PrivateRoomUpdatedMessage> privateUpdatedMessagesBlockingQueue4 = new LinkedBlockingDeque<>();
        session4.subscribe("/user/queue/word", new StompFrameHandler() {
            @Override
            public Type getPayloadType(@Nonnull StompHeaders headers) {
                return PrivateRoomUpdatedMessage.class;
            }

            @Override
            public void handleFrame(@NonNull StompHeaders headers, Object payload) {
                privateUpdatedMessagesBlockingQueue4.add((PrivateRoomUpdatedMessage) payload);
            }
        });

        session.send("/app/room.start", new StartGameMessage(roomId, "host-connection-id"));

        // Then
        RoomUpdatedMessage updatedMessage = updatedMessagesBlockingQueue.poll(2, TimeUnit.SECONDS);
        assertThat(updatedMessage).isNotNull();
        assertThat(updatedMessage.roomId()).isNotNull();
        assertThat(updatedMessage.roomName()).isNotNull();
        assertThat(updatedMessage.playerViewList()).hasSize(4);

        PrivateRoomUpdatedMessage privateMsg = privateUpdatedMessagesBlockingQueue1.poll(5, TimeUnit.SECONDS);
        assertThat(privateMsg).isNotNull();
        assertThat(privateMsg.roomId()).isEqualTo(roomId);
        assertThat(privateMsg.playerId()).isEqualTo("player-id-1");
        assertThat(privateMsg.word()).isNotNull();

        privateMsg = privateUpdatedMessagesBlockingQueue2.poll(5, TimeUnit.SECONDS);
        assertThat(privateMsg).isNotNull();
        assertThat(privateMsg.roomId()).isEqualTo(roomId);
        assertThat(privateMsg.playerId()).isEqualTo("player-id-2");
        assertThat(privateMsg.word()).isNotNull();

        privateMsg = privateUpdatedMessagesBlockingQueue3.poll(5, TimeUnit.SECONDS);
        assertThat(privateMsg).isNotNull();
        assertThat(privateMsg.roomId()).isEqualTo(roomId);
        assertThat(privateMsg.playerId()).isEqualTo("player-id-3");
        assertThat(privateMsg.word()).isNotNull();

        privateMsg = privateUpdatedMessagesBlockingQueue4.poll(5, TimeUnit.SECONDS);
        assertThat(privateMsg).isNotNull();
        assertThat(privateMsg.roomId()).isEqualTo(roomId);
        assertThat(privateMsg.playerId()).isEqualTo("player-id-4");
        assertThat(privateMsg.word()).isNotNull();
    }

    private void assertPlayerJoinedRoom(RoomUpdatedMessage msg, int expectedNumberOfPlayers, String roomId) {
        assertThat(msg).isNotNull();
        assertThat(msg.roomId()).isEqualTo(roomId);
        assertThat(msg.playerViewList()).hasSize(expectedNumberOfPlayers);
    }

}
