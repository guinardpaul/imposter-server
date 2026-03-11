package com.guinardpouard.imposteur.domain.model;

public record PlayerRoundState(
        PlayerId playerId,
        String word,
        boolean impostor) {
}
