package com.botgame.tictactoe;

import com.botgame.general.BoardView;
import com.botgame.general.Bot;

public interface TictactoeBot extends Bot {
  void receive(Mark mark);

  Move turn(BoardView<Mark> board);
}
