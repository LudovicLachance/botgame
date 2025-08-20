package com.botgame.general.board;

import java.util.List;

public class BoardView<Piece extends BoardPiece> implements Board<Piece> {
  private final BoardRaw<Piece> boardRaw;

  public BoardView(BoardRaw<Piece> boardRaw) {
    this.boardRaw = boardRaw;
  }

  @Override
  public BoardNode<Piece> get(int row, int column) {
    return boardRaw.get(row, column);
  }

  @Override
  public List<BoardNode<Piece>> getNodes() {
    return boardRaw.getNodes();
  }
}
