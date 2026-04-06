package com.guinardpouard.imposteur.domain.service;

import com.guinardpouard.imposteur.domain.model.GameSession;
import com.guinardpouard.imposteur.domain.model.Player;
import com.guinardpouard.imposteur.domain.model.WordPair;
import com.guinardpouard.imposteur.domain.port.ApiWordProvider;
import com.guinardpouard.imposteur.infrastructure.persistence.JsonWordProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WordSelectorTests {

    private ApiWordProvider mockApiWordProvider;
    private WordSelector wordSelector;

    @BeforeEach
    void setUp() {
        mockApiWordProvider = mock(JsonWordProvider.class);
        wordSelector = new WordSelector(mockApiWordProvider);
    }

    @Test
    void should_throw_exception_when_no_words_provided() {
        when(mockApiWordProvider.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> wordSelector.select(null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No words available");
    }

    @Test
    void should_select_random_word() {
        List<WordPair> list = List.of(
                new WordPair("a", "b"),
                new WordPair("c","d")
        );
        when(mockApiWordProvider.findAll()).thenReturn(list);

        GameSession gameSession = new GameSession(List.of(Player.player("playerid", "name")));

        WordPair wordPair = wordSelector.select(gameSession);

        assertThat(wordPair).isNotNull();
        assertThat(list).contains(wordPair);
    }

    @Test
    void should_select_different_word_on_two_call() {
        List<WordPair> list = List.of(
                new WordPair("a", "b"),
                new WordPair("c","d")
        );
        when(mockApiWordProvider.findAll()).thenReturn(list);

        GameSession gameSession = new GameSession(List.of(Player.player("playerid", "name")));

        WordPair wordPair = wordSelector.select(gameSession);

        assertThat(list).contains(wordPair);

        WordPair wordPair2 = wordSelector.select(gameSession);

        assertThat(list).contains(wordPair2);
        assertThat(wordPair).isNotEqualTo(wordPair2);
    }

    @Test
    void should_reset_used_words_list_when_all_words_are_used() {
        List<WordPair> list = List.of(
                new WordPair("a", "b"),
                new WordPair("c","d")
        );
        when(mockApiWordProvider.findAll()).thenReturn(list);

        GameSession gameSession = new GameSession(List.of(Player.player("playerid", "name")));

        WordPair wordPair = wordSelector.select(gameSession);

        assertThat(list).contains(wordPair);

        WordPair wordPair2 = wordSelector.select(gameSession);

        assertThat(list).contains(wordPair2);
        assertThat(wordPair).isNotEqualTo(wordPair2);

        WordPair wordPair3 = wordSelector.select(gameSession);

        assertThat(list).contains(wordPair3);
    }

}
