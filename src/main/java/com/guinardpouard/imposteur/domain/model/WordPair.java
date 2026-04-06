package com.guinardpouard.imposteur.domain.model;

public class WordPair {
        private final String commonWord;
        private final String impostorWord;

    public WordPair(String commonWord, String impostorWord) {
        if(commonWord.equals(impostorWord)) {
            throw new IllegalArgumentException("Words must be different");
        }

        this.commonWord = commonWord;
        this.impostorWord = impostorWord;
    }

    public String getCommonWord() {
        return commonWord;
    }

    public String getImpostorWord() {
        return impostorWord;
    }
}
