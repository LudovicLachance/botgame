package com.botgame.quarto;

import com.botgame.general.board.Board;
import com.botgame.general.Bot;

import java.util.List;

public interface QuartoBot extends Bot {
  Move turn(Board<Tower> board, List<Tower> towerToPlay);
}
