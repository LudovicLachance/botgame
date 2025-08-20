package com.playground;

import com.botgame.Player;
import com.botgame.general.Bot;
import com.botgame.general.GameBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreBoardGenerator {
  private static final Logger log = LogManager.getLogger();

  public static void generate(Map<Bot, Integer> scoreboard, String filename) throws IOException {
    var allBots = getAllBots();
    var playerMapping = getPlayerMapping();

    StringBuilder stringBuilder = new StringBuilder("\n");
    stringBuilder.append("<h1>score board<sup><sup>&nbsp;Endless mode</sup></sup></h1>\n\n");

    for (var bot : allBots) {
      stringBuilder.append("## The best %s\n\n".formatted(bot.getSimpleName()));
      stringBuilder.append("|Player|Bot|Score|\n");
      stringBuilder.append("|-|-|-|\n");

      var scoreList = scoreboard.entrySet()
          .stream()
          .filter(e -> bot.isInstance(e.getKey()))
          .sorted(Comparator.comparingInt(Map.Entry<Bot, Integer>::getValue).reversed())
          .toList();


      for (var entry : scoreList) {
        var botName = entry.getKey().getName();
        var playerName = playerMapping.get(botName).getName();
        var score = entry.getValue();
        stringBuilder.append("|%s|%s|%s|\n".formatted(playerName, botName, score));
      }
      stringBuilder.append("\n");
    }

    log.info(stringBuilder.toString());

    Files.writeString(Paths.get(filename), stringBuilder.toString());
  }

  public static List<? extends Class<? extends Bot>> getAllBots() {
    return GamingRoom.getGameBuilders()
        .stream()
        .map(GameBuilder::getBot)
        .toList();
  }

  public static Map<String, Player> getPlayerMapping() {
    Map<String, Player> mapping = new HashMap<>();
    GamingRoom.getPlayers()
        .forEach(player -> {
          player.getBots()
              .forEach(bot -> {
                mapping.putIfAbsent(bot.getName(), player);
              });
        });
    return mapping;
  }
}
