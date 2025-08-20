package com.botgame.tictactoe;

import com.botgame.general.BoardView;
import com.botgame.general.Player;

public interface TictactoePlayer extends Player {
  void receive(Mark mark);

  Move turn(BoardView<Mark> board);
}
