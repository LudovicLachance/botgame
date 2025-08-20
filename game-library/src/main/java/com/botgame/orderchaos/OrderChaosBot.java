package com.botgame.orderchaos;

import com.botgame.general.Bot;
import com.botgame.general.board.Board;

public interface OrderChaosBot extends Bot {
  void receive(Role role);

  Move turn(Board<Mark> board);
}
