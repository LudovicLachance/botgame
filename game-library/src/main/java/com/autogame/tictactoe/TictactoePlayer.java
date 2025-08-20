package com.autogame.tictactoe;

import com.autogame.general.BoardView;
import com.autogame.general.Player;

public interface TictactoePlayer extends Player {
  void receive(Mark mark);

  Move turn(BoardView<Mark> board);
}
