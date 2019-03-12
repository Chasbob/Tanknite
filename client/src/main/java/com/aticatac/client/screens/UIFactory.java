package com.aticatac.client.screens;

import com.aticatac.client.util.Styles;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import java.util.concurrent.Callable;

/**
 * The type Ui factory.
 */
public class UIFactory {
  /**
   * Create title label label.
   *
   * @param text the text
   * @return the label
   */
  static Label createTitleLabel(String text) {
    return new Label(text, Styles.INSTANCE.getTitleStyle());
  }

  /**
   * Create back button text button.
   *
   * @param text the text
   * @return the text button
   */
  static TextButton createBackButton(String text) {
    return new TextButton(text, Styles.INSTANCE.getBackButtonStyle());
  }

  /**
   * Create button text button.
   *
   * @param text the text
   * @return the text button
   */
  static TextButton createButton(String text) {
    return new TextButton(text, Styles.INSTANCE.getBaseButtonStyle());
  }



  /**
   * Create error label label.
   *
   * @param text the text
   * @return the label
   */
  static Label createErrorLabel(String text) {
    return new Label(text, Styles.INSTANCE.getHideLabelStyle());
  }

  /**
   * Create text field text field.
   *
   * @param text the text
   * @return the text field
   */
  static TextField createTextField(String text) {
    return new TextField(text, Styles.INSTANCE.getTextFieldStyle());
  }

  /**
   * Create start button text button.
   *
   * @param text the text
   * @return the text button
   */
  static TextButton createStartButton(String text) {
    return new TextButton(text, Styles.INSTANCE.getStartButtonStyle());
  }

  /**
   * Create label label.
   *
   * @param text the text
   * @return the label
   */
  public static Label createLabel(String text) {
    return new Label(text, Styles.INSTANCE.getBaseLabelStyle());
  }

  /**
   * Create game label label.
   *
   * @param text the text
   * @return the label
   */
  static Label createGameLabel(String text) {
    return new Label(text, Styles.INSTANCE.getGameLabelStyle());
  }

  static Label createColouredLabel(String text){ return new Label(text, Styles.INSTANCE.getColouredLabelStyle());}

  /**
   * New change screen event input listener.
   *
   * @param dstScreen the dst screen
   * @return the input listener
   */
  static InputListener newChangeScreenEvent(Class dstScreen) {
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
          result = func.call();
          //addition of sound
//          Sound tank = Gdx.audio.newSound(Gdx.files.internal("audio/Humvee-Stephan_Schutze-1064024548.mp3"));
//          tank.play();
        } catch (Exception e) {
          e.printStackTrace();
        }
        return result;
      }
    };
  }

  static void newChangeScreenAndReloadEvent(Class dstScreen){
    Screens.INSTANCE.reloadScreen(dstScreen);
    Screens.INSTANCE.showScreen(dstScreen);
  }

}
