package com.guinardpouard.imposteur.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerRoundStateTests {

    @Test
    void should_have_an_id_a_word_and_impostor_state() {
        PlayerId id = PlayerId.newId();
       PlayerRoundState prs = new PlayerRoundState(id, "mot1", false);

       assertThat(prs.playerId()).isEqualTo(id);
       assertThat(prs.word()).isEqualTo("mot1");
       assertThat(prs.impostor()).isEqualTo(false);
    }
}
