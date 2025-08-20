package com.botgame.tictactoe;

import com.botgame.general.board.BoardPiece;

public enum Mark implements BoardPiece {
  X, O;

  @Override
  public String view() {
    return this.name();
  }
}
