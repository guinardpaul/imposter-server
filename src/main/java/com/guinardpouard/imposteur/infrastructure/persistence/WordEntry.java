package com.guinardpouard.imposteur.infrastructure.persistence;

public class WordEntry {

    private final String common;
    private final String impostor;

    public WordEntry(String common, String impostor) {
        this.common = common;
        this.impostor = impostor;
    }

    public String getCommon() {
        return common;
    }

    public String getImpostor() {
        return impostor;
    }

}
