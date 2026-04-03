package com.guinardpouard.imposteur.infrastructure.persistence;

import com.guinardpouard.imposteur.domain.model.WordPair;
import com.guinardpouard.imposteur.domain.port.ApiWordProvider;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WordProviderTests {

    @Test
    void should_return_wordPair() {
        ApiWordProvider provider = new WordProvider();
        List<WordPair> list = provider.getWords(1);

        assertThat(list).hasSize(1);
        assertThat(list.getFirst().commonWord()).isNotNull();
        assertThat(list.getFirst().impostorWord()).isNotNull();
    }

}
