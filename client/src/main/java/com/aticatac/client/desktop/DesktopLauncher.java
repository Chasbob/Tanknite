package com.aticatac.client.desktop;

import com.aticatac.client.game.GDXGame;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * The type Desktop launcher.
 */
public class DesktopLauncher {
  /**
   * The entry point of application.
   *
   * @param arg the input arguments
   */
  public static void main(String[] arg) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    new LwjglApplication(new GDXGame(), config);
    config.width = 1000;
    config.height = 1000;
//    config.fullscreen = true;
//    config.resizable = false;
  }
}
