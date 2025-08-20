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
  private final TictactoeBot bot1;
  private final TictactoeBot bot2;
  private final List<Bot> winners = new ArrayList<>();

  public Tictactoe(TictactoeBot bot1, TictactoeBot bot2) {
    this.bot1 = bot1;
    this.bot2 = bot2;
  }

  public static boolean noMorePlaceToPlay(Board<Mark> board) {
    return board.getNodes()
        .stream().noneMatch(node -> node.piece().isEmpty());
  }

  public static boolean winCondition(Board<Mark> board, List<Bot> winners, TictactoeBot bot1, TictactoeBot bot2) {
    Supplier<Stream<List<BoardNode<Mark>>>> lines = () -> board.getLines()
        .stream()
        .filter(line -> line.size() >= 3);

    var botXWin = lines.get()
        .anyMatch(line -> line
            .stream()
            .map(BoardNode::piece)
            .allMatch(mark -> mark.isPresent() && mark.orElseThrow() == Mark.X));
    var botOWin = lines.get()
        .anyMatch(line -> line
            .stream()
            .map(BoardNode::piece)
            .allMatch(mark -> mark.isPresent() && mark.orElseThrow() == Mark.O));

    if (botXWin) {
      winners.add(bot1);
    }

    if (botOWin) {
      winners.add(bot2);
    }

    return botOWin || botXWin;
  }

  @Override
  public List<Bot> start() {
    bot1.receive(Mark.X);
    bot2.receive(Mark.O);

    while (true) {
      {
        var botPlayed = botTurn(bot1);
        if (winCondition(board, winners, bot1, bot2) || noMorePlaceToPlay(board)) {
          return winners;
        }
      }
      {
        var botPlayed = botTurn(bot2);
        if (winCondition(board, winners, bot1, bot2) || noMorePlaceToPlay(board)) {
          return winners;
        }
      }
    }
  }

  private boolean botTurn(TictactoeBot bot) {
    var move = bot.turn(new BoardView<>(board));
    var moveIsDone = board.makeMove(move.mark(), move.row(), move.col());
    log.debug(board.show(move.row(), move.col()));
    return moveIsDone;
  }

  public static class Builder extends GameBuilder {
    @Override
    public List<Game> combinatory(List<Bot> bots) {
      List<Game> games = new ArrayList<>();
      for (var combination : Combination.combine(filter(bots, TictactoeBot.class), 2)) {
        games.add(new Tictactoe(combination.getFirst(), combination.getLast()));
        games.add(new Tictactoe(combination.getLast(), combination.getFirst()));
      }
      return games;
    }
  }
}
