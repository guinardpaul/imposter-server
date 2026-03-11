package com.guinardpouard.imposteur.infrastructure.websocket.publisher;

import com.guinardpouard.imposteur.application.port.GamePublisher;
import com.guinardpouard.imposteur.application.event.PrivateRoomUpdatedMessage;
import com.guinardpouard.imposteur.application.event.RoomUpdatedMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WebSocketGamePublisher implements GamePublisher {

    private final SimpMessagingTemplate template;

    public WebSocketGamePublisher(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public void publishRoomCreated(RoomUpdatedMessage msg) {
        template.convertAndSend(
                "/topic/room",
                msg
        );
    }

    @Override
    public void publishRoomJoined(RoomUpdatedMessage msg) {
        template.convertAndSend(
                "/topic/room/" + msg.roomId() + "/players",
                msg
        );
    }

    @Override
    public void publishGameStarted(RoomUpdatedMessage msg) {
        template.convertAndSend(
                "/topic/room/" + msg.roomId(),
                msg
        );
    }

    @Override
    public void sendWordToPlayer(PrivateRoomUpdatedMessage msg) {
        template.convertAndSendToUser(
                msg.playerId(),
                "/queue/word",
                msg
        );
    }
}
