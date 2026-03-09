package com.guinardpouard.imposteur.infrastructure.websocket.entrypoint;

import com.guinardpouard.imposteur.infrastructure.websocket.entrypoint.dto.CreateRoomMessage;
import com.guinardpouard.imposteur.infrastructure.websocket.entrypoint.dto.JoinRoomMessage;
import com.guinardpouard.imposteur.application.event.RoomUpdatedMessage;
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
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

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
    void whenPlayerCreatesNewRoom_shouldReceiveRoomCreatedMessage() throws Exception {
        BlockingQueue<RoomUpdatedMessage> blockingQueue = new LinkedBlockingDeque<>();

        StompSession session = stompClient
                .connectAsync(websocketUrl, new StompSessionHandlerAdapter() {})
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

        StompSession session = stompClient
                .connectAsync(websocketUrl, new StompSessionHandlerAdapter() {})
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

}
