package com.guinardpouard.imposteur.domain.repository;

import com.guinardpouard.imposteur.domain.model.WordPair;

import java.util.List;

public interface ApiWordProvider {

    List<WordPair> getWords(int count);
}
