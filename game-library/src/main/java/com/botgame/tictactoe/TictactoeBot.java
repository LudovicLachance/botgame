package com.botgame.tictactoe;

import com.botgame.general.board.Board;
import com.botgame.general.Bot;

public interface TictactoeBot extends Bot {
  void receive(Mark mark);

  Move turn(Board<Mark> board);
}
