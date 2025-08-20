package com.botgame.tictactoe;

import com.botgame.Combination;
import com.botgame.general.*;
import com.botgame.general.board.BoardNode;
import com.botgame.general.board.BoardRaw;
import com.botgame.general.board.BoardView;
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

  private final BoardRaw<Mark> boardRaw = new BoardRaw<>(3, 3);
  private final TictactoeBot bot1;
  private final TictactoeBot bot2;
  private final List<Bot> winners = new ArrayList<>();

  public Tictactoe(TictactoeBot bot1, TictactoeBot bot2) {
    this.bot1 = bot1;
    this.bot2 = bot2;
  }

  public static boolean noMorePlaceToPlay(BoardRaw<Mark> boardRaw) {
    return boardRaw.getNodes()
        .stream().noneMatch(node -> node.piece().isEmpty());
  }

  public static boolean winCondition(BoardRaw<Mark> boardRaw, List<Bot> winners, TictactoeBot bot1, TictactoeBot bot2) {
    Supplier<Stream<List<BoardNode<Mark>>>> lines = () -> boardRaw.getLines()
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
        if (winCondition(boardRaw, winners, bot1, bot2) || noMorePlaceToPlay(boardRaw)) {
          return winners;
        }
      }
      {
        var botPlayed = botTurn(bot2);
        if (winCondition(boardRaw, winners, bot1, bot2) || noMorePlaceToPlay(boardRaw)) {
          return winners;
        }
      }
    }
  }

  private boolean botTurn(TictactoeBot bot) {
    var move = bot.turn(new BoardView<>(boardRaw));
    var moveIsDone = boardRaw.makeMove(move.mark(), move.row(), move.col());
    log.debug(boardRaw.show(move.row(), move.col()));
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

    @Override
    public Class<? extends Bot> getBot() {
      return TictactoeBot.class;
    }
  }
}
