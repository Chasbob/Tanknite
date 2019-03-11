package com.aticatac.server.test;

import com.aticatac.common.model.Command;
import com.aticatac.server.components.GameManager;

public interface Game {
  void addPlayer(String player);
  void removePlayer(String player);
  void startGame();
  void playerInput(String player, Command command);

}
