package com.guinardpouard.imposteur.domain.service;

import com.guinardpouard.imposteur.domain.model.GameSession;
import com.guinardpouard.imposteur.domain.model.WordPair;
import com.guinardpouard.imposteur.domain.port.ApiWordProvider;

import java.util.List;
import java.util.Random;

public class WordSelector {

    private final ApiWordProvider apiWordProvider;
    private final Random random = new Random();

    public WordSelector(ApiWordProvider apiWordProvider) {
        this.apiWordProvider = apiWordProvider;
    }

    public WordPair select(GameSession session) {
        List<WordPair> allWords = apiWordProvider.findAll();

        if (allWords.isEmpty()) {
            throw new IllegalStateException("No words available");
        }

        session.resetIfNeeded(allWords.size());

        int index;
        do {
            index = random.nextInt(allWords.size());
        } while (session.hasUsed(index));

        session.markUsed(index);

        return allWords.get(index);
    }
}
