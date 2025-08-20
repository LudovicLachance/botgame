package com.playground.players.base;

import com.botgame.Player;
import com.botgame.general.Bot;

import java.util.ArrayList;
import java.util.List;

public class BasePlayer implements Player {
  @Override
  public String getName() {
    return "BasePlayer";
  }

  @Override
  public List<Bot> getBots() {
    List<Bot> bots = new ArrayList<>();
    bots.add(new TicTacToeRandom());
    bots.add(new OrderChaosRandom());
    return bots;
  }
}
