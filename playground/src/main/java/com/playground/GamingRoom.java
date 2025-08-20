package com.playground;

import com.autogame.Tournament;
import com.autogame.general.GameBuilder;
import com.autogame.general.Player;
import com.autogame.quarto.Quarto;
import com.autogame.tictactoe.Tictactoe;
import com.playground.players.sooluckyseven.TicTacToeRandomSLS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class GamingRoom {
  private static final Logger log = LogManager.getLogger();

  public static void main(String[] args) {
    try {
      Map<Player, Integer> totalScores = new HashMap<>();
      List<GameBuilder> gameBuilders = getGameBuilders();
      List<Player> players = getPlayers();
      for (int i = 0; i < 1000; i++) {
        for (GameBuilder gameBuilder : gameBuilders) {
          Tournament tournament = new Tournament(players, gameBuilder);
          tournament.start().forEach((key, value) ->
              totalScores.merge(key, value, Integer::sum)
          );
        }
      }

      var scoreboard = totalScores.entrySet()
          .stream().sorted(Comparator.comparingInt(Map.Entry<Player, Integer>::getValue).reversed())
          .toList();
      for (var entry : scoreboard) {
        log.info("%s %s".formatted(entry.getKey().getName(), entry.getValue()));
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public static List<GameBuilder> getGameBuilders() {
    List<GameBuilder> gameBuilders = new ArrayList<>();
    gameBuilders.add(new Tictactoe.Builder());
    gameBuilders.add(new Quarto.Builder());
    return gameBuilders;
  }

  public static List<Player> getPlayers() {
    List<Player> players = new ArrayList<>();
    players.add(new TicTacToeRandomSLS(String.valueOf(0x0000)));
    players.add(new TicTacToeRandomSLS(String.valueOf(0x0001)));
    players.add(new TicTacToeRandomSLS(String.valueOf(0x0002)));
    players.add(new TicTacToeRandomSLS(String.valueOf(0x0003)));
    players.add(new TicTacToeRandomSLS(String.valueOf(0x0004)));
    players.add(new TicTacToeRandomSLS(String.valueOf(0x0005)));
    return players;
  }
}
