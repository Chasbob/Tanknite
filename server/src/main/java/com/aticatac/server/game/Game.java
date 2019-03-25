package com.aticatac.server.game;

import com.aticatac.common.model.CommandModel;
import com.aticatac.common.model.Updates.Update;

public interface Game {
  void initialise();
  void addPlayer(String player);

  Update getFrame();

  void removePlayer(String player);

  void startGame();

  void gameOver();

  void playerInput(CommandModel model);
}
