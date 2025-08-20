package com.botgame.quarto;

import com.botgame.general.BoardView;
import com.botgame.general.Player;

import java.util.List;

public interface QuartoPlayer extends Player {
  Move turn(BoardView<Tower> board, List<Tower> towerToPlay);
}
