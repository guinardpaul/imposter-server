package com.guinardpouard.imposteur.websocket.dto;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RoomUpdatedMessageTests {

    @Test
    void roomUpdatedMessage_should_contains_id_and_name_and_playerView_List() {
        List<PlayerView> playerViewList = new ArrayList<>();
        playerViewList.add(new PlayerView("playerId", "playerName"));
        RoomUpdatedMessage msg = new RoomUpdatedMessage("roomId", "roomName", playerViewList);

        assertThat(msg.roomId()).isEqualTo("roomId");
        assertThat(msg.roomName()).isEqualTo("roomName");
        assertThat(msg.playerViewList()).hasSize(1);
    }
}
