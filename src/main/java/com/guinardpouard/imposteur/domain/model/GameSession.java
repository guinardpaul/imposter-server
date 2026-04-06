package com.guinardpouard.imposteur.domain.model;

import java.util.*;

public class GameSession {

    private final List<Player> players;
    private final List<Round> rounds = new ArrayList<>();
    private final Set<Integer> usedWordIndexes = new HashSet<>();

    public GameSession(List<Player> players) {
        this.players = players;
    }

    public void startNextRound(WordPair wordPair) {
        Round round = Round.create(players, wordPair);

        rounds.add(round);
    }

    public Round getCurrentRound() {
        return rounds.getLast();
    }

    public boolean hasUsed(int index) {
        return usedWordIndexes.contains(index);
    }

    public void markUsed(int index) {
        usedWordIndexes.add(index);
    }

    public void resetIfNeeded(int totalSize) {
        if (usedWordIndexes.size() >= totalSize) {
            usedWordIndexes.clear();
        }
    }

}
