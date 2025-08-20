package com.playground.players.base;

import com.botgame.general.board.Board;
import com.botgame.orderchaos.Mark;
import com.botgame.orderchaos.Move;
import com.botgame.orderchaos.OrderChaosBot;
import com.botgame.orderchaos.Role;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class OrderChaosRandom implements OrderChaosBot {
  Role role;
  Random rng = ThreadLocalRandom.current();

  @Override
  public String getName() {
    return "OrderChaosRandom";
  }

  @Override
  public void receive(Role role) {
    this.role = role;
  }

  @Override
  public Move turn(Board<Mark> board) {
    var empty = board.getNodes()
        .stream().filter(node -> node.piece().isEmpty())
        .toList();
    var index = rng.nextInt(0, empty.size());
    var node = empty.get(index);
    Mark mark = rng.nextBoolean() ? Mark.O : Mark.X;
    return new Move(mark, node.row(), node.column());
  }
}
