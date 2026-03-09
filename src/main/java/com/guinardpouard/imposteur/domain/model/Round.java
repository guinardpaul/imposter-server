package com.guinardpouard.imposteur.domain.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Round {

    private final Map<PlayerId, PlayerRoundState> states;

    private Round(Map<PlayerId, PlayerRoundState> states) {
        this.states = states;
    }

    public static Round create(List<Player> players, WordPair wordPair) {
        Player impostor = pickRandom(players);

        Map<PlayerId, PlayerRoundState> states = new HashMap<>();

        for (Player player : players) {
            boolean isImpostor = player.id().equals(impostor.id());

            String word = isImpostor ? wordPair.impostorWord() : wordPair.commonWord();

            states.put(
                    player.id(),
                    new PlayerRoundState(player.id(), word, isImpostor));
        }
        return new Round(states);
    }

    public PlayerRoundState getStateFor(PlayerId id) {
        return states.get(id);
    }

    public boolean isImpostor(PlayerId id) {
        return  states.get(id).impostor();
    }

    public Map<PlayerId, PlayerRoundState> getStates() {
        return states;
    }

    private static Player pickRandom(List<Player> players) {
        return players.get(ThreadLocalRandom.current().nextInt(players.size()));
    }
}
