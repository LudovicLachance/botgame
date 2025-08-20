package com.botgame.orderchaos;

import com.botgame.Combination;
import com.botgame.general.Bot;
import com.botgame.general.Game;
import com.botgame.general.GameBuilder;
import com.botgame.general.board.BoardNode;
import com.botgame.general.board.BoardRaw;
import com.botgame.general.board.BoardView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class OrderChaos implements Game {
  private final OrderChaosBot order;
  private final OrderChaosBot chaos;
  private final BoardRaw<Mark> boardRaw = new BoardRaw<>(6, 6);

  public OrderChaos(OrderChaosBot order, OrderChaosBot chaos) {
    this.order = order;
    this.chaos = chaos;
  }

  public static boolean chaosWin(BoardRaw<Mark> boardRaw) {
    return boardRaw.getNodes()
        .stream().noneMatch(node -> node.piece().isEmpty());
  }

  public static boolean orderWin(BoardRaw<Mark> boardRaw) {
    Supplier<Stream<List<BoardNode<Mark>>>> lines = () -> boardRaw.getLines()
        .stream()
        .filter(line -> line.size() >= 5);

    var x5 = lines.get()
        .anyMatch(line -> line
            .stream()
            .map(BoardNode::piece)
            .allMatch(mark -> mark.isPresent() && mark.orElseThrow() == Mark.X));
    var o5 = lines.get()
        .anyMatch(line -> line
            .stream()
            .map(BoardNode::piece)
            .allMatch(mark -> mark.isPresent() && mark.orElseThrow() == Mark.O));

    return x5 || o5;
  }

  @Override
  public List<Bot> start() {
    order.receive(Role.Order);
    chaos.receive(Role.Chaos);

    while (true) {
      {
        var move = order.turn(new BoardView<>(boardRaw));
        boardRaw.makeMove(move.mark(), move.row(), move.column());
        if (orderWin(boardRaw)) {
          return List.of(order);
        }
        if (chaosWin(boardRaw)) {
          return List.of(chaos);
        }
      }
      {
        var move = chaos.turn(new BoardView<>(boardRaw));
        boardRaw.makeMove(move.mark(), move.row(), move.column());
        if (orderWin(boardRaw)) {
          return List.of(order);
        }
        if (chaosWin(boardRaw)) {
          return List.of(chaos);
        }
      }
    }
  }

  public static class Builder extends GameBuilder {
    @Override
    public List<Game> combinatory(List<Bot> bots) {
      List<Game> games = new ArrayList<>();
      for (var combination : Combination.combine(filter(bots, OrderChaosBot.class), 2)) {
        games.add(new OrderChaos(combination.getFirst(), combination.getLast()));
        games.add(new OrderChaos(combination.getLast(), combination.getFirst()));
      }
      return games;
    }

    @Override
    public Class<? extends Bot> getBot() {
      return OrderChaosBot.class;
    }
  }
}
