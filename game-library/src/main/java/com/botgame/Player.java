package com.botgame;

import com.botgame.general.Bot;

import java.util.List;

public interface Player {
  String getName();
  List<Bot> getBots();
}
