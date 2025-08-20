package com.botgame.general;

import java.util.List;

public abstract class GameBuilder {
  public abstract List<Game> combinatory(List<Bot> bots);

  public <T> List<T> filter(List<Bot> bots, Class<T> type) {
    return bots
        .stream()
        .filter(type::isInstance)
        .map(type::cast)
        .toList();
  }
}
