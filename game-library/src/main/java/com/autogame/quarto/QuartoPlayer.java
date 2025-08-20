package com.autogame.quarto;

import com.autogame.general.BoardView;
import com.autogame.general.Player;

import java.util.List;

public interface QuartoPlayer extends Player {
  Move turn(BoardView<Tower> board, List<Tower> towerToPlay);
}
