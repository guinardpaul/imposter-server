package com.guinardpouard.imposteur.infrastructure.websocket.entrypoint;

import com.guinardpouard.imposteur.application.RoomService;
import com.guinardpouard.imposteur.domain.model.Room;
import com.guinardpouard.imposteur.application.mapper.RoomUpdatedMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameListenerTests {

    @Mock
    private SimpMessagingTemplate template;
    @Mock
    private RoomService roomService;
    @Mock
    private RoomUpdatedMapper roomUpdatedMapper;
    @InjectMocks
    private GameListener gameListener;

    @Test
    void should_send_message_when_user_subscribe_to_queue_game_info() {
        // Create STOMP headers
        StompHeaderAccessor accessor =
                StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        accessor.setSessionId("session1");
        accessor.setDestination("/user/queue/game-info");

        Message<byte[]> message =
                MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());

        SessionSubscribeEvent event =
                new SessionSubscribeEvent(this, message);

        when(roomService.getAllRooms())
                .thenReturn(List.of(
                                new Room("room1", "hostId")
                        )
                );
        when(roomUpdatedMapper.toMessage(any())).thenCallRealMethod();
        SimpMessageHeaderAccessor headerAccessor =
                SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId("session1");

        // Act
        gameListener.handleSubscribe(event);

        // Assert
        verify(template).convertAndSendToUser(
                eq("session1"),
                eq("/queue/game-info"),
                anyCollection(),
                eq(headerAccessor.getMessageHeaders())
        );
    }
}
