package com.guinardpouard.imposteur.websocket;

import com.guinardpouard.imposteur.application.GameService;
import com.guinardpouard.imposteur.websocket.mapper.RoomUpdatedMapper;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class GameListener {

    private final SimpMessagingTemplate template;
    private final GameService gameService;
    private final RoomUpdatedMapper roomUpdatedMapper;

    public GameListener(SimpMessagingTemplate template, GameService gameService,
                        RoomUpdatedMapper roomUpdatedMapper) {
        this.template = template;
        this.gameService = gameService;
        this.roomUpdatedMapper = roomUpdatedMapper;
    }

    @EventListener
    public void handleSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = accessor.getDestination();
        String sessionId = accessor.getSessionId();

        if ("/user/queue/game-info".equals(destination) && sessionId != null) {
            template.convertAndSendToUser(
                    sessionId,
                    "/queue/game-info",
                     gameService
                             .getAllRooms()
                             .stream()
                             .map(roomUpdatedMapper::toMessage)
                             .toList(),
                    createHeaders(sessionId)
            );
        }
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor =
                SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

}
