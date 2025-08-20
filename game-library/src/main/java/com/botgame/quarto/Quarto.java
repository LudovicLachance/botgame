package com.botgame.quarto;

import com.botgame.Combination;
import com.botgame.general.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://en.wikipedia.org/wiki/Quarto_(board_game)">Quarto (board game)</a>
 */
public class Quarto implements Game {
  private final Board<Tower> board = new Board<>(4, 4);
  private final List<Tower> towers = new ArrayList<>(16);
  private final QuartoBot bot1;
  private final QuartoBot bot2;

  public Quarto(QuartoBot bot1, QuartoBot bot2) {
    this.bot1 = bot1;
    this.bot2 = bot2;

    for (Tower.Height height : List.of(Tower.Height.Short, Tower.Height.Tall)) {
      for (Tower.Color color : List.of(Tower.Color.Blue, Tower.Color.Red)) {
        for (Tower.Shape shape : List.of(Tower.Shape.Circular, Tower.Shape.Square)) {
          for (Tower.Interior interior : List.of(Tower.Interior.Hollow, Tower.Interior.Solid)) {
            towers.add(new Tower(height, color, shape, interior));
          }
        }
      }
    }
  }

  @Override
  public List<Bot> start() {
    List<Bot> winners = new ArrayList<>();

    while (true) {
      {
        var botPlayed = botTurn(bot1);
        if (winCondition()) {
          winners.add(bot1);
          return winners;
        }
      }
      {
        var botPlayed = botTurn(bot2);
        if (winCondition()) {
          winners.add(bot2);
          return winners;
        }
      }
    }
  }

  public boolean winCondition() {
    return true;
  }

  private boolean botTurn(QuartoBot bot) {
    var move = bot.turn(new BoardView<>(board), List.copyOf(towers));
    var pieceExist = towers.remove(move.tower());
    if (!pieceExist) {
      return false;
    }

    return board.makeMove(move.tower(), move.row(), move.col());
  }

  public static class Builder extends GameBuilder {
    @Override
    public List<Game> combinatory(List<Bot> bots) {
      List<Game> games = new ArrayList<>();
      for (var combination : Combination.combine(filter(bots, QuartoBot.class), 2)) {
        games.add(new Quarto(combination.getFirst(), combination.getLast()));
      }
      return games;
    }

    @Override
    public Class<? extends Bot> getBot() {
      return QuartoBot.class;
    }
  }
}
