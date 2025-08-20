package com.playground;

import com.botgame.BotTournament;
import com.botgame.Player;
import com.botgame.general.Bot;
import com.botgame.general.GameBuilder;
import com.botgame.quarto.Quarto;
import com.botgame.tictactoe.Tictactoe;
import com.playground.players.sooluckyseven.SooLuckySeven;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GamingRoom {
  private static final Logger log = LogManager.getLogger();

  public static void main(String[] args) {

    try {
      Map<Bot, Integer> scoreboard = new HashMap<>();
      List<GameBuilder> gameBuilders = getGameBuilders();
      List<Bot> bots = getBots();
      for (int i = 0; i < 1000; i++) {
        for (GameBuilder gameBuilder : gameBuilders) {
          BotTournament botTournament = new BotTournament(bots, gameBuilder);
          botTournament.start().forEach((key, value) ->
              scoreboard.merge(key, value, Integer::sum)
          );
        }
      }

      String filename;
      if (args.length > 0) {
        filename = args[0];
        ScoreBoardGenerator.generate(scoreboard, filename);
      } else {
        ScoreBoardGenerator.generate(scoreboard);
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

  public static List<Bot> getBots() {
    return getPlayers()
        .stream()
        .map(Player::getBots)
        .flatMap(List::stream)
        .toList();
  }

  public static List<Player> getPlayers() {
    List<Player> players = new ArrayList<>();
    players.add(new SooLuckySeven());
    return players;
  }
}
