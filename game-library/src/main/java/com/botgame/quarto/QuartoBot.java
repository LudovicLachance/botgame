package com.botgame.quarto;

import com.botgame.general.BoardView;
import com.botgame.general.Bot;

import java.util.List;

public interface QuartoBot extends Bot {
  Move turn(BoardView<Tower> board, List<Tower> towerToPlay);
}
