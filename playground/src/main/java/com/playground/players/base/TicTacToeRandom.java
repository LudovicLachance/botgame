package com.playground.players.base;

import com.botgame.general.board.Board;
import com.botgame.tictactoe.Mark;
import com.botgame.tictactoe.Move;
import com.botgame.tictactoe.TictactoeBot;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TicTacToeRandom implements TictactoeBot {
  Mark mark;
  Random rng = ThreadLocalRandom.current();

  @Override
  public String getName() {
    return "TicTacToeRandom";
  }

  @Override
  public void receive(Mark mark) {
    this.mark = mark;
  }

  @Override
  public Move turn(Board<Mark> board) {
    var empty = board.getNodes()
        .stream().filter(node -> node.piece().isEmpty())
        .toList();
    var index = rng.nextInt(0, empty.size());
    var node = empty.get(index);
    return new Move(mark, node.row(), node.column());
  }
}
