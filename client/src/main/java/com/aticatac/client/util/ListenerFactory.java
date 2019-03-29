package com.aticatac.client.util;

import com.aticatac.client.screens.Screens;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.concurrent.Callable;

/**
 * The type Listener factory.
 */
public class ListenerFactory {

  /**
   * New change screen event input listener.
   *
   * @param dstScreen the dst screen
   * @return the input listener
   */
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

  /**
   * New listener event input listener.
   *
   * @param func the func
   * @return the input listener
   */
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

  /**
   * Add hover listener.
   *
   * @param button      the button
   * @param parentTable the parent table
   */
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

  /**
   * Add hover listener.
   *
   * @param button      the button
   * @param parentTable the parent table
   */
  public static void addHoverListener(TextButton button, Table parentTable) {
    button.addListener(newListenerEventEnter(() -> {
      Styles.INSTANCE.addTableColour(parentTable, Color.CORAL);
      return false;
    }));
    button.addListener(newListenerEventExit(() -> {
      Styles.INSTANCE.addTableColour(parentTable, Styles.INSTANCE.getTransparentColour());
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

  /**
   * New change screen and reload event.
   *
   * @param dstScreen the dst screen
   */
  public static void newChangeScreenAndReloadEvent(Class dstScreen) {
    Screens.INSTANCE.reloadScreen(dstScreen);
    Screens.INSTANCE.showScreen(dstScreen);
  }

}
