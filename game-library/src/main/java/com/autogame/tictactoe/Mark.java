package com.autogame.tictactoe;

import com.autogame.general.BoardPiece;

public enum Mark implements BoardPiece {
  X, O;

  @Override
  public String view() {
    return this.name();
  }
}
