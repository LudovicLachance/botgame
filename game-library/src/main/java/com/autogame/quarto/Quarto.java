package com.autogame.quarto;

import com.autogame.Combination;
import com.autogame.general.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://en.wikipedia.org/wiki/Quarto_(board_game)">Quarto (board game)</a>
 */
public class Quarto implements Game {
  private final Board<Tower> board = new Board<>(4, 4);
  private final List<Tower> towers = new ArrayList<>(16);
  private final QuartoPlayer player1;
  private final QuartoPlayer player2;

  public Quarto(QuartoPlayer player1, QuartoPlayer player2) {
    this.player1 = player1;
    this.player2 = player2;

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
  public List<Player> start() {
    List<Player> winners = new ArrayList<>();

    while (true) {
      {
        var playerPlayed = playerTurn(player1);
        if (winCondition()) {
          winners.add(player1);
          return winners;
        }
      }
      {
        var playerPlayed = playerTurn(player2);
        if (winCondition()) {
          winners.add(player2);
          return winners;
        }
      }
    }
  }

  public boolean winCondition() {
    return true;
  }

  private boolean playerTurn(QuartoPlayer player) {
    var move = player.turn(new BoardView<>(board), List.copyOf(towers));
    var pieceExist = towers.remove(move.tower());
    if (!pieceExist) {
      return false;
    }

    return board.makeMove(move.tower(), move.row(), move.col());
  }

  public static class Builder extends GameBuilder {
    @Override
    public List<Game> combinatory(List<Player> players) {
      List<Game> games = new ArrayList<>();
      for (var combination : Combination.combine(filter(players, QuartoPlayer.class), 2)) {
        games.add(new Quarto(combination.getFirst(), combination.getLast()));
      }
      return games;
    }
  }
}
