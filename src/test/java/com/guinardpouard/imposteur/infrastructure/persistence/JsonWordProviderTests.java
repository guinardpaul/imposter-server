package com.guinardpouard.imposteur.infrastructure.persistence;

import com.guinardpouard.imposteur.domain.model.WordPair;
import com.guinardpouard.imposteur.domain.port.ApiWordProvider;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JsonWordProviderTests {


    @Test
    void should_return_wordPair() {
        ApiWordProvider provider = new JsonWordProvider();
        List<WordPair> list = provider.getWords(1);

        assertThat(list).hasSize(1);
        assertThat(list.getFirst().commonWord()).isNotNull();
        assertThat(list.getFirst().impostorWord()).isNotNull();
    }

    @Test
    void should_return_all_words () {
        JsonWordProvider provider = new JsonWordProvider();
        provider.loadWords();
        List<WordPair> list = provider.findAll();

        assertThat(list).hasSize(6);
    }

}
