package com.botgame.general.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @see <a href="https://en.wikipedia.org/wiki/Board_game">Board game</a>
 */
public class BoardRaw<Piece extends BoardPiece> implements Board<Piece> {
  private static final String RED = "\u001B[31m";
  private static final String GREEN = "\u001B[32m";
  private static final String RESET = "\u001B[0m";
  private final List<Piece> boardPieces;
  private final int numberRow;
  private final int numberColumn;

  public BoardRaw(int numberRow, int numberColumn) {
    this.numberRow = numberRow;
    this.numberColumn = numberColumn;

    boardPieces = new ArrayList<>(numberRow * numberColumn);
    for (int r = 0; r < numberRow; r++) {
      for (int c = 0; c < numberColumn; c++) {
        boardPieces.add(null);
      }
    }
  }

  @Override
  public BoardNode<Piece> get(int row, int column) {
    if (row >= numberRow) {
      row = row % numberRow;
    }

    if (column >= numberColumn) {
      column = column % numberColumn;
    }

    if (row <= -numberRow) {
      row = row % -numberRow;
    }

    if (column <= -numberColumn) {
      column = column % -numberColumn;
    }

    if (row < 0) {
      row += numberRow;
    }

    if (column < 0) {
      column += numberColumn;
    }

    var pos = row * numberColumn + column;

    Piece boardPiece = boardPieces.get(pos);

    if (boardPiece == null) {
      return new BoardNode<>(Optional.empty(), row, column);
    } else {
      return new BoardNode<>(Optional.of(boardPieces.get(pos)), row, column);
    }
  }

  public boolean makeMove(Piece piece, int row, int column) {
    if (get(row, column).piece().isEmpty()) {
      var pos = row * numberColumn + column;

      boardPieces.set(pos, piece);
      return true;
    }
    return false;
  }

  @Override
  public List<BoardNode<Piece>> getNodes() {
    List<BoardNode<Piece>> nodes = new ArrayList<>(boardPieces.size());
    for (int r = 0; r < numberRow; r++) {
      for (int c = 0; c < numberColumn; c++) {
        nodes.add(get(r, c));
      }
    }
    return nodes;
  }

  private int getNumberOfLines(int xsquare) {
    int n = xsquare * 2;
    return n + ((n - 1) * 2);
  }

  public List<List<BoardNode<Piece>>> getLines() {
    int numberOfLine = 0;

    if (numberRow == numberColumn) {
      numberOfLine = getNumberOfLines(numberRow);
    }

    List<List<BoardNode<Piece>>> lines = new ArrayList<>(numberOfLine);

    for (int r = 0; r < numberRow; r++) {
      List<BoardNode<Piece>> hLine = new ArrayList<>();
      List<BoardNode<Piece>> vLine = new ArrayList<>();
      for (int c = 0; c < numberColumn; c++) {
        hLine.add(get(r, c));
        vLine.add(get(c, r));
      }
      lines.add(hLine);
      lines.add(vLine);
    }

    lines.addAll(getDiagonals(this));
    lines.addAll(getDiagonals(new RotatedBoard<>(this)));

    return lines;
  }

  private List<List<BoardNode<Piece>>> getDiagonals(Board<Piece> board) {
    int m = numberRow;
    int n = numberColumn;
    List<List<BoardNode<Piece>>> diagonals = new ArrayList<>();

    // Start from each element in the first row
    for (int col = 0; col < n; col++) {
      List<BoardNode<Piece>> diagonal = new ArrayList<>();
      int i = 0, j = col;
      while (i < m && j < n) {
        diagonal.add(board.get(i, j));
        i++;
        j++;
      }
      diagonals.add(diagonal);
    }

    // Start from each element in the first column (except [0][0], already covered)
    for (int row = 1; row < m; row++) {
      List<BoardNode<Piece>> diagonal = new ArrayList<>();
      int i = row, j = 0;
      while (i < m && j < n) {
        diagonal.add(board.get(i, j));
        i++;
        j++;
      }
      diagonals.add(diagonal);
    }

    return diagonals;
  }

  public String show(int newRow, int newColumn) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int row = 0; row < numberRow; row++) {
      stringBuilder.append("\n");
      for (int col = 0; col < numberColumn; col++) {
        var node = get(row, col);
        if (node.piece().isEmpty()) {
          stringBuilder.append("n");
        } else if (row == newRow && col == newColumn) {
          stringBuilder.append(RED);
          stringBuilder.append(node.piece().orElseThrow().view());
          stringBuilder.append(RESET);
        } else {
          stringBuilder.append(GREEN);
          stringBuilder.append(node.piece().orElseThrow().view());
          stringBuilder.append(RESET);
        }
        stringBuilder.append(" ");
      }
    }
    return stringBuilder.toString();
  }
}
