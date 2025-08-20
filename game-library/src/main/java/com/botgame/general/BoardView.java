package com.botgame.general;

import java.util.List;
import java.util.Optional;

public class BoardView<Piece extends BoardPiece> {
  private final Board<Piece> board;

  public BoardView(Board<Piece> board) {
    this.board = board;
  }

  public Optional<Piece> get(int row, int column) {
    return board.get(row, column);
  }

  public List<BoardNode<Piece>> getNodes() {
    return board.getNodes();
  }
}
