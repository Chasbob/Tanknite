package com.aticatac.desktop;

import com.aticatac.client.game.SinglePlayer;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class SinglePlayerTest {
  public static void main(String[] arg) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    new LwjglApplication(new SinglePlayer(), config);
//        config.width = 640;
//        config.height = 640;
//        config.fullscreen=true;
  }
}
