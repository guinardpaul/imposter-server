package com.guinardpouard.imposteur.domain.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class GameSession {

    private final Map<Player, Role> roles;

    public GameSession(List<Player> players) {
        this.roles = assignRoles(players);
    }

    private Map<Player, Role> assignRoles(List<Player> players) {
        Map<Player, Role> map = new HashMap<>();

        Player impostor = players.get(ThreadLocalRandom.current().nextInt(players.size()));

        for (Player p : players) {
            map.put(p, p.equals(impostor) ? Role.IMPOSTOR : Role.CREWMATE);
        }
        return map;
    }

    public boolean isImpostor(Player player) {
        return roles.get(player) == Role.IMPOSTOR;
    }

}
