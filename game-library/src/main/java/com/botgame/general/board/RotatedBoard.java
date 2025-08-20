package com.botgame.general.board;

import java.util.List;

public class RotatedBoard<Piece extends BoardPiece> implements Board<Piece> {
  private final Board<Piece> board;

  public RotatedBoard(Board<Piece> board) {
    this.board = board;
  }

  @Override
  public BoardNode<Piece> get(int row, int column) {
    return board.get(-column - 1, row);
  }

  @Override
  public List<BoardNode<Piece>> getNodes() {
    return board.getNodes();
  }
}
