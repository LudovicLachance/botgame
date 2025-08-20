package com.autogame;

import com.autogame.general.Game;
import com.autogame.general.GameBuilder;
import com.autogame.general.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tournament {
  private final List<Player> players;
  private final GameBuilder gameBuilder;

  public Tournament(List<Player> players, GameBuilder gameBuilder) {
    this.players = players;
    this.gameBuilder = gameBuilder;
  }

  public Map<Player, Integer> start() {
    Map<Player, Integer> scores = new HashMap<>();

    for (Game game : gameBuilder.combinatory(players)) {
      var winners = game.start();
      for (Player player : winners) {
        scores.putIfAbsent(player, 0);
        scores.computeIfPresent(player, (_, val) -> val + 1);
      }
    }

    return scores;
  }
}
