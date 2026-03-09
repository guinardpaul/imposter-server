package com.guinardpouard.imposteur.infrastructure.websocket.publisher;

import com.guinardpouard.imposteur.application.event.PlayerView;
import com.guinardpouard.imposteur.application.event.PrivateRoomUpdatedMessage;
import com.guinardpouard.imposteur.application.event.RoomUpdatedMessage;
import com.guinardpouard.imposteur.application.port.GamePublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WebSocketGamePublisherTests {

    @Mock
    private SimpMessagingTemplate template;
    private GamePublisher publisher;

    @BeforeEach
    void setup() {
        publisher = new WebSocketGamePublisher(template);
    }

    @Test
    void should_send_room_created_event() {
        // Given
        PlayerView p1 = new PlayerView("id1", "name1");
        PlayerView p2 = new PlayerView("id2", "name2");
        PlayerView p3 = new PlayerView("id3", "name3");
        List<PlayerView> list = List.of(p1, p2, p3);
        RoomUpdatedMessage msg = new RoomUpdatedMessage(
                "roomId",
                "room1",
                list
        );
        // When
        publisher.publishRoomCreated(msg);
        // Then
        verify(template, times(1))
                .convertAndSend(
                        "/topic/room",
                        msg
                );
    }

    @Test
    void should_send_event_when_user_join_room() {
        //Given
        PlayerView p1 = new PlayerView("id1", "name1");
        PlayerView p2 = new PlayerView("id2", "name2");
        PlayerView p3 = new PlayerView("id3", "name3");
        List<PlayerView> list = List.of(p1, p2, p3);
        RoomUpdatedMessage msg = new RoomUpdatedMessage(
                "roomId",
                "room1",
                list
        );
        // When
        publisher.publishRoomJoined(msg);
        // Then
        verify(template, times(1))
                .convertAndSend(
                        "/topic/room/roomId/players",
                        msg
                );
    }

    @Test
    void sendWordToPlayer_should_send_word_to_each_player() {
        PrivateRoomUpdatedMessage msg =  new PrivateRoomUpdatedMessage(
                "room id",
                "player id",
                "Avion"
        );
        publisher.sendWordToPlayer(msg);

        verify(template, times(1))
                .convertAndSendToUser(
                        eq("player id"),
                        eq("/queue/word"),
                        eq(msg)
                );
    }
}
