package com.playground;

import com.botgame.general.Bot;
import com.botgame.general.GameBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;

public class ScoreBoardGenerator {
  private static final Logger log = LogManager.getLogger();

  public static void generate(Map<Bot, Integer> scoreboard, String filename) throws IOException {
    var allBots = GamingRoom.getGameBuilders()
        .stream()
        .map(GameBuilder::getBot)
        .toList();

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
        stringBuilder.append("|-|%s|%s|\n".formatted(entry.getKey().getName(), entry.getValue()));
      }
      stringBuilder.append("\n");
    }

    log.info(stringBuilder.toString());

    Files.writeString(Paths.get(filename), stringBuilder.toString());
  }
}
