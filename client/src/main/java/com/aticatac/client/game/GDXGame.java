package com.aticatac.client.game;

import com.aticatac.client.screens.Screens;
import com.aticatac.client.server.networking.Server;
import com.aticatac.common.model.Updates.Update;
import com.badlogic.gdx.Game;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static com.aticatac.client.bus.EventBusFactory.eventBus;

/**
 * The type Gdx game.
 */
public class GDXGame extends Game {
  private static Server server;
  private BlockingQueue<Update> updates;

  /**
   * Create server.
   *
   * @param singleplayer the singleplayer
   * @param id           the id
   */
  public static void createServer(boolean singleplayer, String id) {
    if (server != null) {
      server.shutdown();
    }
    server = new Server(singleplayer, id);
    server.start();
  }

  /**
   * Instantiates a new Gdx game.
   */
  public GDXGame() {
    eventBus.register(this);
  }

  /**
   * Stop server.
   */
  public static void stopServer() {
    if (server != null) {
      server.shutdown();
    }
  }

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
