package com.autogame.general;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @see <a href="https://en.wikipedia.org/wiki/Board_game">Board game</a>
 */
public class Board<Piece extends BoardPiece> {
  private static final String RED = "\u001B[31m";
  private static final String GREEN = "\u001B[32m";
  private static final String RESET = "\u001B[0m";
  private final List<Piece> boardPieces;
  private final int numberRow;
  private final int numberColumn;

  public Board(int numberRow, int numberColumn) {
    this.numberRow = numberRow;
    this.numberColumn = numberColumn;

    boardPieces = new ArrayList<>(numberRow * numberColumn);
    for (int r = 0; r < numberRow; r++) {
      for (int c = 0; c < numberColumn; c++) {
        boardPieces.add(null);
      }
    }
  }

  public Optional<Piece> get(int row, int column) {
    boolean rowTest = row < 0 || row >= numberRow;
    boolean columnTest = column < 0 || column >= numberColumn;

    if (rowTest || columnTest) {
      return Optional.empty();
    }

    var pos = row * numberColumn + column;
    Piece boardPiece = boardPieces.get(pos);

    if (boardPiece == null) {
      return Optional.empty();
    } else {
      return Optional.of(boardPieces.get(pos));
    }
  }

  public boolean makeMove(Piece piece, int row, int column) {
    if (get(row, column).isEmpty()) {
      var pos = row * numberColumn + column;

      boardPieces.set(pos, piece);
      return true;
    }
    return false;
  }

  public List<BoardNode<Piece>> getNodes() {
    List<BoardNode<Piece>> nodes = new ArrayList<>(boardPieces.size());
    for (int r = 0; r < numberRow; r++) {
      for (int c = 0; c < numberColumn; c++) {
        nodes.add(new BoardNode<>(get(r, c), r, c));
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
        hLine.add(new BoardNode<>(get(r, c), r, c));
        vLine.add(new BoardNode<>(get(c, r), c, r));
      }
      lines.add(hLine);
      lines.add(vLine);
    }

    lines.addAll(getPrimaryDiagonals());
    lines.addAll(getSecondaryDiagonals());

    return lines;
  }

  private List<List<BoardNode<Piece>>> getPrimaryDiagonals() {
    int m = numberRow;
    int n = numberColumn;
    List<List<BoardNode<Piece>>> diagonals = new ArrayList<>();

    // Start from each element in the first row
    for (int col = 0; col < n; col++) {
      List<BoardNode<Piece>> diagonal = new ArrayList<>();
      int i = 0, j = col;
      while (i < m && j < n) {
        diagonal.add(new BoardNode<>(get(i, j), i, j));
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
        diagonal.add(new BoardNode<>(get(i, j), i, j));
        i++;
        j++;
      }
      diagonals.add(diagonal);
    }

    return diagonals;
  }

  private List<List<BoardNode<Piece>>> getSecondaryDiagonals() {
    int m = numberRow;
    int n = numberColumn;
    List<List<BoardNode<Piece>>> diagonals = new ArrayList<>();

    // Start from each element in the first row
    for (int col = 0; col < n; col++) {
      List<BoardNode<Piece>> diagonal = new ArrayList<>();
      int i = 0, j = col;
      while (i < m && j >= 0) {
        diagonal.add(new BoardNode<>(get(i, j), i, j));
        i++;
        j--;
      }
      diagonals.add(diagonal);
    }

    // Start from each element in the last column (except [0][n-1], already covered)
    for (int row = 1; row < m; row++) {
      List<BoardNode<Piece>> diagonal = new ArrayList<>();
      int i = row, j = n - 1;
      while (i < m && j >= 0) {
        diagonal.add(new BoardNode<>(get(i, j), i, j));
        i++;
        j--;
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
        var piece = get(row, col);
        if (piece.isEmpty()) {
          stringBuilder.append("n");
        } else if (row == newRow && col == newColumn) {
          stringBuilder.append(RED);
          stringBuilder.append(piece.orElseThrow().view());
          stringBuilder.append(RESET);
        } else {
          stringBuilder.append(GREEN);
          stringBuilder.append(piece.orElseThrow().view());
          stringBuilder.append(RESET);
        }
        stringBuilder.append(" ");
      }
    }
    return stringBuilder.toString();
  }
}
