package com.aticatac.client.util;

import com.aticatac.client.screens.Screens;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.concurrent.Callable;

public class ListenerFactory {

  public static InputListener newChangeScreenEvent(Class dstScreen) {
    return new InputListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        try {
          Screens.INSTANCE.showScreen(dstScreen);
        } catch (Exception e) {
          e.printStackTrace();
        }
        return false;
      }
    };
  }

  public static InputListener newListenerEvent(Callable<Boolean> func) {
    return new InputListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Boolean result = false;
        try {
          result = func.call();
        } catch (Exception e) {
          e.printStackTrace();
        }
        return result;
      }
    };
  }

  public static void newChangeScreenAndReloadEvent(Class dstScreen) {
    Screens.INSTANCE.reloadScreen(dstScreen);
    Screens.INSTANCE.showScreen(dstScreen);
  }

}