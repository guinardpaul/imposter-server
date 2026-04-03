package com.guinardpouard.imposteur.infrastructure.persistence;

import com.guinardpouard.imposteur.domain.model.WordPair;
import com.guinardpouard.imposteur.domain.port.ApiWordProvider;

import java.util.List;

public class WordProvider implements ApiWordProvider {

    @Override
    public List<WordPair> getWords(int count) {
        return List.of(new WordPair("Banane", "Avion"));
    }
}
