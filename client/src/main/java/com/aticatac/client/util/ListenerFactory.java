package com.aticatac.client.util;

import com.aticatac.client.screens.Screens;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

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
          AudioEnum.INSTANCE.getButtonClick();
          result = func.call();
        } catch (Exception e) {
          e.printStackTrace();
        }
        return result;
      }
    };
  }

  private static ClickListener newListenerEventEnter(Callable<Boolean> func) {
    return new ClickListener() {
      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        listenerBody(func);
      }
    };
  }

  private static ClickListener newListenerEventExit(Callable<Boolean> func) {
    return new ClickListener() {
      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        listenerBody(func);
      }
    };
  }

  public static void addHoverListener(TextButton button, MenuTable parentTable) {
    button.addListener(newListenerEventEnter(() -> {
      Styles.INSTANCE.addTableColour(parentTable, Color.CORAL);
      return false;
    }));
    button.addListener(newListenerEventExit(() -> {
      parentTable.setShowGroup(parentTable.isShowGroup());
      return false;
    }));
  }

  private static void listenerBody(Callable<Boolean> func) {
    try {
      func.call();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void newChangeScreenAndReloadEvent(Class dstScreen) {
    Screens.INSTANCE.reloadScreen(dstScreen);
    Screens.INSTANCE.showScreen(dstScreen);
  }

}
