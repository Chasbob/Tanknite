package com.aticatac.desktop;

import com.aticatac.client.game.GDXGame;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
  public static void main(String[] arg) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    new LwjglApplication(new GDXGame(), config);
    config.width = 640;
    config.height = 640;
  }
}
