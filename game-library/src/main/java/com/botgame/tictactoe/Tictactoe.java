package com.botgame.tictactoe;

import com.botgame.Combination;
import com.botgame.general.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @see <a href="https://en.wikipedia.org/wiki/Tic-tac-toe">Tic-tac-toe</a>
 */
public class Tictactoe implements Game {
  private static final Logger log = LogManager.getLogger();

  private final Board<Mark> board = new Board<>(3, 3);
  private final TictactoePlayer player1;
  private final TictactoePlayer player2;
  private final List<Player> winners = new ArrayList<>();

  public Tictactoe(TictactoePlayer player1, TictactoePlayer player2) {
    this.player1 = player1;
    this.player2 = player2;
  }

  public static boolean noMorePlaceToPlay(Board<Mark> board) {
    return board.getNodes()
        .stream().noneMatch(node -> node.piece().isEmpty());
  }

  public static boolean winCondition(Board<Mark> board, List<Player> winners, TictactoePlayer player1, TictactoePlayer player2) {
    Supplier<Stream<List<BoardNode<Mark>>>> lines = () -> board.getLines()
        .stream()
        .filter(line -> line.size() >= 3);

    var playerXWin = lines.get()
        .anyMatch(line -> line
            .stream()
            .map(BoardNode::piece)
            .allMatch(mark -> mark.isPresent() && mark.orElseThrow() == Mark.X));
    var playerOWin = lines.get()
        .anyMatch(line -> line
            .stream()
            .map(BoardNode::piece)
            .allMatch(mark -> mark.isPresent() && mark.orElseThrow() == Mark.O));

    if (playerXWin) {
      winners.add(player1);
    }

    if (playerOWin) {
      winners.add(player2);
    }

    return playerOWin || playerXWin;
  }

  @Override
  public List<Player> start() {
    player1.receive(Mark.X);
    player2.receive(Mark.O);

    while (true) {
      {
        var playerPlayed = playerTurn(player1);
        if (winCondition(board, winners, player1, player2) || noMorePlaceToPlay(board)) {
          return winners;
        }
      }
      {
        var playerPlayed = playerTurn(player2);
        if (winCondition(board, winners, player1, player2) || noMorePlaceToPlay(board)) {
          return winners;
        }
      }
    }
  }

  private boolean playerTurn(TictactoePlayer player) {
    var move = player.turn(new BoardView<>(board));
    var moveIsDone = board.makeMove(move.mark(), move.row(), move.col());
    log.debug(board.show(move.row(), move.col()));
    return moveIsDone;
  }

  public static class Builder extends GameBuilder {
    @Override
    public List<Game> combinatory(List<Player> players) {
      List<Game> games = new ArrayList<>();
      for (var combination : Combination.combine(filter(players, TictactoePlayer.class), 2)) {
        games.add(new Tictactoe(combination.getFirst(), combination.getLast()));
        games.add(new Tictactoe(combination.getLast(), combination.getFirst()));
      }
      return games;
    }
  }
}
