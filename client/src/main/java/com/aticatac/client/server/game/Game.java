package com.aticatac.client.server.game;

import com.aticatac.common.model.CommandModel;
import com.aticatac.common.model.Updates.Update;

/**
 * The interface Game.
 */
public interface Game {
  /**
   * Initialise.
   */
  void initialise();

  /**
   * Add player.
   *
   * @param player the player
   */
  void addPlayer(String player);

  /**
   * Gets frame.
   *
   * @return the frame
   */
  Update getFrame();

  /**
   * Remove player.
   *
   * @param player the player
   */
  void removePlayer(String player);

  /**
   * Start game.
   */
  void startGame();

  /**
   * Game over.
   */
  void gameOver();

  /**
   * Player input.
   *
   * @param model the model
   */
  void playerInput(CommandModel model);
}
