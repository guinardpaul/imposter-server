package com.guinardpouard.imposteur.application.port;

import com.guinardpouard.imposteur.application.event.PrivateRoomUpdatedMessage;
import com.guinardpouard.imposteur.application.event.RoomUpdatedMessage;

public interface GamePublisher {

    void publishRoomCreated(RoomUpdatedMessage msg);
    void publishRoomJoined(RoomUpdatedMessage msg);
    void publishGameStarted(PrivateRoomUpdatedMessage msg);
    void sendWordToPlayer(PrivateRoomUpdatedMessage msg);
}
