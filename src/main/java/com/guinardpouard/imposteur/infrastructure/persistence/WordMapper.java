package com.guinardpouard.imposteur.infrastructure.persistence;

import com.guinardpouard.imposteur.domain.model.WordPair;

public class WordMapper {

    public WordPair toDomain (WordEntry wordEntry) {
        return new WordPair(
                wordEntry.getCommon(),
                wordEntry.getImpostor()
        );
    }
}
