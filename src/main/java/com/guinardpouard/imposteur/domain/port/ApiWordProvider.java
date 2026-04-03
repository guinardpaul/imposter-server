package com.guinardpouard.imposteur.domain.port;

import com.guinardpouard.imposteur.domain.model.WordPair;

import java.util.List;

public interface ApiWordProvider {

    List<WordPair> getWords(int count);
}
