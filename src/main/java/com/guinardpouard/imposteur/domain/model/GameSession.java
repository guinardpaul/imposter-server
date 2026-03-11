package com.guinardpouard.imposteur.domain.model;

import java.util.ArrayList;
import java.util.List;

public class GameSession {

    private final List<Player> players;
    private final List<Round> rounds = new ArrayList<>();

    public GameSession(List<Player> players, WordPair wordPair) {
        this.players = players;

        startNextRound(wordPair);
    }

    public void startNextRound(WordPair wordPair) {
        Round round = Round.create(players, wordPair);

        rounds.add(round);
    }

    public Round getCurrentRound() {
        return rounds.getLast();
    }
}
