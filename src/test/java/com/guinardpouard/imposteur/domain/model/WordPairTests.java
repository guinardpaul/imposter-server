package com.guinardpouard.imposteur.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WordPairTests {

    @Test
    void words_should_be_different() {
        assertThatThrownBy(() -> new WordPair("a", "a"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Words must be different");
    }
}
