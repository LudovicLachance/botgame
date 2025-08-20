package com.botgame;

import com.botgame.general.Bot;
import com.botgame.general.Game;
import com.botgame.general.GameBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BotTournament {
  private final List<Bot> bots;
  private final GameBuilder gameBuilder;

  public BotTournament(List<Bot> bots, GameBuilder gameBuilder) {
    this.bots = bots;
    this.gameBuilder = gameBuilder;
  }

  public Map<Bot, Integer> start() {
    Map<Bot, Integer> scores = new HashMap<>();

    for (Game game : gameBuilder.combinatory(bots)) {
      var winners = game.start();
      for (Bot bot : winners) {
        scores.putIfAbsent(bot, 0);
        scores.computeIfPresent(bot, (_, val) -> val + 1);
      }
    }

    return scores;
  }
}
