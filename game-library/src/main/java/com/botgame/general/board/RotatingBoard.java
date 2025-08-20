package com.botgame.general.board;

import java.util.List;

public class RotatingBoard<Piece extends BoardPiece> implements Board<Piece> {
  private final BoardView<Piece> boardView;

  public RotatingBoard(BoardView<Piece> boardView) {
    this.boardView = boardView;
  }

  @Override
  public BoardNode<Piece> get(int row, int column) {
    return null;
  }

  @Override
  public List<BoardNode<Piece>> getNodes() {
    return boardView.getNodes();
  }
}
