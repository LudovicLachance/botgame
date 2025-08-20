package com.botgame.quarto;

import com.botgame.general.board.BoardPiece;

public record Tower(Height height, Color color, Shape shape, Interior interior) implements BoardPiece {
  @Override
  public String view() {
    char symbol = 0x2800;

    if (height == Height.Short) {
      symbol |= 0x01;
    }

    if (color == Color.Blue) {
      symbol |= 0x02;
    }

    if (shape == Shape.Circular) {
      symbol |= 0x03;
    }

    if (interior == Interior.Hollow) {
      symbol |= 0x04;
    }

    return String.valueOf(symbol);
  }

  public enum Height {
    Tall,
    Short
  }

  public enum Color {
    Red,
    Blue
  }

  public enum Shape {
    Square,
    Circular
  }

  public enum Interior {
    Hollow,
    Solid
  }
}
