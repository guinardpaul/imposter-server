package com.guinardpouard.imposteur.infrastructure.websocket.entrypoint;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    private final static Logger log = LoggerFactory.getLogger(WebSocketChannelInterceptor.class);

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        StompCommand command = accessor.getCommand();
        if (StompCommand.CONNECT.equals(command)) {
            handleConnect(accessor);
        }
        if (StompCommand.DISCONNECT.equals(command)) {
            handleDisconnect(accessor);
        }

        return message;
    }

    private void handleConnect(StompHeaderAccessor accessor) {
        String playerId = accessor.getFirstNativeHeader("player-id");

        if (playerId == null) {
            String msg = "player-id header is required";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        Principal principal = new UsernamePasswordAuthenticationToken(playerId, null);
        accessor.setUser(principal);

        log.info("WS CONNECT player={}", playerId);
    }

    private void handleDisconnect(StompHeaderAccessor accessor) {
        Principal principal = accessor.getUser();

        if (principal != null) {
            log.info("WS DISCONNECT player={}", principal.getName());
        }
    }
}
