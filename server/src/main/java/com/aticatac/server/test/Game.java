package com.aticatac.server.test;

import com.aticatac.common.model.CommandModel;

public interface Game {
  void addPlayer(String player);

  void removePlayer(String player);

  void startGame();

  void playerInput(CommandModel model);
}
