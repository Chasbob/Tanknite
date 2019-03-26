package com.aticatac.client.game;

import com.aticatac.client.screens.Screens;
import com.aticatac.common.model.Updates.Update;
import com.badlogic.gdx.Game;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * The type Single player game.
 */
public class SinglePlayer extends Game {
  private BlockingQueue<Update> updates;

  @Override
  public void create() {
    try {
      this.updates = new ArrayBlockingQueue<>(100);
      //TODO show splash screen whilst it loads
      Screens.INSTANCE.initialize(this);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void dispose() {
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);
  }
}
