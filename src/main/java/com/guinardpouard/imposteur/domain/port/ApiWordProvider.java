package com.guinardpouard.imposteur.domain.port;

import com.guinardpouard.imposteur.domain.model.WordPair;
import com.guinardpouard.imposteur.infrastructure.persistence.WordEntry;

import java.util.List;

public interface ApiWordProvider {

    List<WordPair> findAll();
    List<WordPair> getWords(int count);
}
