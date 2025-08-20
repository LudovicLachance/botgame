package com.autogame.general;

import java.util.List;

public abstract class GameBuilder {
  public abstract List<Game> combinatory(List<Player> players);

  public <T> List<T> filter(List<Player> players, Class<T> type) {
    return players
        .stream()
        .filter(type::isInstance)
        .map(type::cast)
        .toList();
  }
}
