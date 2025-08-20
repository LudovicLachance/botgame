package com.playground.players.sooluckyseven;

import com.botgame.general.BoardView;
import com.botgame.tictactoe.Mark;
import com.botgame.tictactoe.Move;
import com.botgame.tictactoe.TictactoeBot;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TicTacToeRandomSLS implements TictactoeBot {
  private final String name;
  Mark mark;
  Random rng = ThreadLocalRandom.current();

  public TicTacToeRandomSLS(String name) {
    this.name = name;
  }

  @Override
  public void receive(Mark mark) {
    this.mark = mark;
  }

  @Override
  public Move turn(BoardView<Mark> board) {
    var empty = board.getNodes()
        .stream().filter(node -> node.piece().isEmpty())
        .toList();
    var index = rng.nextInt(0, empty.size());
    var node = empty.get(index);
    return new Move(mark, node.row(), node.column());
  }

  @Override
  public String getName() {
    return "TicTacToeRandom: %s".formatted(name);
  }
}
