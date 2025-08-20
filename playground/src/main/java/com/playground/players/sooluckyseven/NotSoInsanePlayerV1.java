package com.playground.players.sooluckyseven;

import com.botgame.general.board.Board;
import com.botgame.general.board.BoardNode;
import com.botgame.orderchaos.Mark;
import com.botgame.orderchaos.Move;
import com.botgame.orderchaos.OrderChaosBot;
import com.botgame.orderchaos.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class NotSoInsanePlayerV1 implements OrderChaosBot {
  private static final int BOARD_SIZE = 6;
  Role role;

  @Override
  public String getName() {
    return "InsanePlayer";
  }

  @Override
  public void receive(Role role) {
    this.role = role;
  }

  @Override
  public Move turn(Board<Mark> board) {
    if (role == Role.Order) {
      return playOrder(board);
    } else {
      return playChaos(board);
    }
  }

  public Move playOrder(Board<Mark> board) {
    var b = new MBoard(board);
    var bestLineToPlayIn = b.sortedLines.first();
    var X = bestLineToPlayIn.numberOfX;
    var O = bestLineToPlayIn.numberOfO;
    var selectedSquare = bestLineToPlayIn.getBest();
    if (X > O) {
      return selectedSquare.makeMove(Mark.X);
    } else {
      return selectedSquare.makeMove(Mark.O);
    }
  }


  public Move playChaos(Board<Mark> board) {
    var b = new MBoard(board);
    var bestLineToPlayIn = b.sortedLines.first();
    var X = bestLineToPlayIn.numberOfX;
    var O = bestLineToPlayIn.numberOfO;
    var selectedSquare = bestLineToPlayIn.getBest();
    if (X > O) {
      return selectedSquare.makeMove(Mark.O);
    } else {
      return selectedSquare.makeMove(Mark.X);
    }
  }

  public static class MBoard {
    public final List<SquareInfo> squareInfoList = new ArrayList<>();
    public final List<Line> allLines = new ArrayList<>();
    public final TreeSet<Line> sortedLines;

    public MBoard(Board<Mark> board) {
      for (int row = 0; row < BOARD_SIZE; row++) {
        for (int col = 0; col < BOARD_SIZE; col++) {
          squareInfoList.add(new SquareInfo(board.get(row, col)));
        }
      }

      List<Line> rows = new ArrayList<>();
      List<Line> cols = new ArrayList<>();
      for (int i = 0; i < BOARD_SIZE; i++) {
        rows.add(new Line(List.of(
            at(i, 0), at(i, 1), at(i, 2),
            at(i, 3), at(i, 4), at(i, 5))));
        cols.add(new Line(List.of(
            at(0, i), at(1, i), at(2, i),
            at(3, i), at(4, i), at(5, i))));
      }


      List<Line> diagonals = new ArrayList<>();
      diagonals.add(new Line(List.of(
          at(0, 0), at(1, 1), at(2, 2),
          at(3, 3), at(4, 4), at(5, 5))));
      diagonals.add(new Line(List.of(
          at(5, 0), at(4, 1), at(3, 2),
          at(2, 3), at(1, 4), at(0, 5))));

      diagonals.add(new Line(List.of(
          at(0, 1), at(1, 2), at(2, 3),
          at(3, 4), at(4, 5))));
      diagonals.add(new Line(List.of(
          at(1, 0), at(2, 1), at(3, 2),
          at(4, 3), at(5, 4))));

      diagonals.add(new Line(List.of(
          at(5, 1), at(4, 2), at(3, 3),
          at(2, 4), at(1, 5))));
      diagonals.add(new Line(List.of(
          at(4, 0), at(3, 1), at(2, 2),
          at(1, 3), at(0, 4))));

      allLines.addAll(rows);
      allLines.addAll(cols);
      allLines.addAll(diagonals);

      sortedLines = new TreeSet<>(allLines);
    }

    private SquareInfo at(int row, int col) {
      return squareInfoList.get(row * BOARD_SIZE + col);
    }

    public Line Line(List<SquareInfo> items) {
      return new Line(items);
    }

    public final class SquareInfo implements Comparable<SquareInfo> {
      private final BoardNode<Mark> node;
      private Long score = null;

      public SquareInfo(BoardNode<Mark> node) {
        this.node = node;
      }

      public Move makeMove(Mark mark) {
        return new Move(mark, node.row(), node.column());
      }

      public long getScore() {
        if (score == null) {
          score = allLines.stream()
              .filter(line -> line.items.contains(this)).count();
        }
        return -score;
      }

      @Override
      public int compareTo(SquareInfo other) {
        if (node.piece().isEmpty()) {
          return -1;
        }
        return Long.compare(getScore(), other.getScore());
      }
    }

    public class Line implements Comparable<Line> {
      public final int maxInRowOfX;
      public final int maxInRowOfO;
      public final int numberOfX;
      public final int numberOfO;
      public final int numberOfNull;
      private final List<SquareInfo> items = new ArrayList<>();
      private Long score = null;
      private int skipLevel = 0;

      private Line(List<SquareInfo> items) {
        int tmpX = 0;
        int tmpO = 0;
        int tmpNull = 0;
        for (var item : items) {
          this.items.add(item);
          if (item.node.piece().isPresent() && item.node.piece().get().equals(Mark.X)) {
            tmpX++;
          }
          if (item.node.piece().isPresent() && item.node.piece().get().equals(Mark.O)) {
            tmpO++;
          }
          if (item.node.piece().isEmpty()) {
            tmpNull++;
          }
        }
        maxInRowOfX = getMaxInARowOf(Mark.X);
        maxInRowOfO = getMaxInARowOf(Mark.O);
        numberOfX = tmpX;
        numberOfO = tmpO;
        numberOfNull = tmpNull;

        if (Math.max(numberOfX, numberOfO) > 3 && numberOfNull > 0) {
          skipLevel += 2;
        }

        boolean has3notSame = (numberOfX > 0 && numberOfO > 1) || (numberOfX > 1 && numberOfO > 0);
        boolean has5uninterrupted = !(has3notSame)
            || (Math.max(numberOfX, numberOfO) > 2 && numberOfNull > 2)
            || (Math.max(maxInRowOfX, maxInRowOfO) > 2 && numberOfNull > 1);

        if (has5uninterrupted) {
          skipLevel++;
        }

        boolean level1skip = (numberOfO == 1 && maxInRowOfX > 2 && numberOfX > 3 && numberOfNull > 0)
            || (numberOfX == 1 && maxInRowOfO > 2 && numberOfO > 3 && numberOfNull > 0)
            || (Math.max(numberOfX, numberOfO) > 3 && numberOfNull > 1);

        if (level1skip) {
          skipLevel++;
        }

        var first = items.getFirst().node.piece();
        var last = items.getLast().node.piece();

        boolean notStartOrEndO = first.orElse(null) != Mark.O && last.orElse(null) != Mark.O;
        boolean notStartOrEndX = first.orElse(null) != Mark.X && last.orElse(null) != Mark.X;

        boolean level2skip = (level1skip && notStartOrEndO)
            || (level1skip && notStartOrEndX);

        if (level2skip) {
          skipLevel++;
        }
      }

      private int getMaxInARowOf(Mark mark) {
        int maximum = 0;

        int accumulator = 0;

        for (var item : items) {
          if (item.node.piece().isPresent() && item.node.piece().get().equals(mark)) {
            accumulator++;
            maximum = Math.max(maximum, accumulator);
          } else {
            accumulator = 0;
          }
        }

        return maximum;
      }

      @Override
      public int compareTo(Line other) {
        if (other.numberOfNull == 0) {
          return -1;
        }

        if (numberOfNull == 0) {
          return 1;
        }

        if (numberOfO > 1 && numberOfX > 1) {
          return 1;
        }

        return Long.compare(getScore(), other.getScore());
      }

      public List<MBoard.SquareInfo> getNull() {
        return items.stream()
            .filter(s -> s.node.piece().isEmpty())
            .toList();
      }

      public SquareInfo getBest() {
        if (skipLevel > 0) {
          int pos = 2;
          if (items.get(pos).node.piece().orElse(null) == null) {
            return items.get(pos);
          }
          pos = 3;
          if (items.get(pos).node.piece().orElse(null) == null) {
            return items.get(pos);
          }
          pos = 1;
          if (items.get(pos).node.piece().orElse(null) == null) {
            return items.get(pos);
          }
          pos = 4;
          if (items.get(pos).node.piece().orElse(null) == null) {
            return items.get(pos);
          }
          pos = 0;
          if (items.get(pos).node.piece().orElse(null) == null) {
            return items.get(pos);
          }
          pos = 5;
          if (items.get(pos).node.piece().orElse(null) == null) {
            return items.get(pos);
          }
        }
        var nulls = getNull();
        return nulls.stream()
            .max(SquareInfo::compareTo)
            .orElse(nulls.getFirst());
      }

      private long getScore() {
        if (score == null) {
          score = 0L;
          if (skipLevel > 0) {
            score += skipLevel * 20L;
          }
          score += Math.max(maxInRowOfO, maxInRowOfX);
          score += Math.max(numberOfO, numberOfX);
          score += numberOfNull;
        }
        return -score;
      }
    }
  }
}
