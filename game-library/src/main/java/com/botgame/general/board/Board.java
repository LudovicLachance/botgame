package com.botgame.general.board;

import java.util.List;

public interface Board<Piece extends BoardPiece> {
  BoardNode<Piece> get(int row, int column);
  List<BoardNode<Piece>> getNodes();
}
