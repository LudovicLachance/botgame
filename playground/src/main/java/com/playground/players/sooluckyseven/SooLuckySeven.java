package com.playground.players.sooluckyseven;

import com.botgame.Player;
import com.botgame.general.Bot;

import java.util.ArrayList;
import java.util.List;

public class SooLuckySeven implements Player {
  @Override
  public String getName() {
    return "SooLuckySeven";
  }

  @Override
  public List<Bot> getBots() {
    List<Bot> bots = new ArrayList<>();
    bots.add(new TicTacToeRandomV1());
    bots.add(new NotSoInsanePlayerV1());
    return bots;
  }
}
