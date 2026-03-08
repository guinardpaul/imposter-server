package com.guinardpouard.imposteur.domain.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerIdTests {

    @Test
    void newId_should_generate_a_new_uuid() {
        PlayerId id = PlayerId.newId();

        assertThat(id).isNotNull();
        assertThat(id.getValue()).isNotNull();
        assertThat(UUID.fromString(id.getValue())).isInstanceOf(UUID.class);
    }

    @Test
    void of_should_create_PlayerId_with_given_uuid() {
        UUID uuid = UUID.randomUUID();

        PlayerId id = PlayerId.of(uuid.toString());

        assertThat(id).isNotNull();
        assertThat(id.getValue()).isEqualTo(uuid.toString());
    }
}
