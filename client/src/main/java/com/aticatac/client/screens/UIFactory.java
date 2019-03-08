package com.aticatac.client.screens;

import com.aticatac.client.util.ListServers;
import com.aticatac.client.util.PopulatePlayers;
import com.aticatac.client.util.ServerButton;
import com.aticatac.client.util.Styles;
import com.aticatac.common.model.ServerInformation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
  public static Label createTitleLabel(String text) {
    return new Label(text, Styles.INSTANCE.getTitleStyle());
  }

  /**
   * Create back button text button.
   *
   * @param text the text
   * @return the text button
   */
  public static TextButton createBackButton(String text) {
    return new TextButton(text, Styles.INSTANCE.getBackButtonStyle());
  }

  /**
   * Create server button server button.
   *
   * @param text              the text
   * @param serverInformation the server information
   * @return the server button
   */
  public static ServerButton createServerButton(String text, ServerInformation serverInformation) {
    return new ServerButton(text, Styles.INSTANCE.getButtonStyle(), serverInformation);
  }

  /**
   * Create button text button.
   *
   * @param text the text
   * @return the text button
   */
  public static TextButton createButton(String text) {
    return new TextButton(text, Styles.INSTANCE.getButtonStyle());
  }

  /**
   * Gets servers.
   *
   * @param serversTable the servers table
   */
  public static void getServers(Table serversTable) {
    (new ListServers(serversTable)).start();
  }

  /**
   * Create error label label.
   *
   * @param text the text
   * @return the label
   */
  public static Label createErrorLabel(String text) {
    return new Label(text, Styles.INSTANCE.getHideLabelStyle());
  }

  /**
   * Create text field text field.
   *
   * @param text the text
   * @return the text field
   */
  public static TextField createTextField(String text) {
    return new TextField(text, Styles.INSTANCE.getTextFieldStyle());
  }

  /**
   * Create start button text button.
   *
   * @param text the text
   * @return the text button
   */
  public static TextButton createStartButton(String text) {
    return new TextButton(text, Styles.INSTANCE.getStartButtonStyle());
  }

  /**
   * Populate lobby.
   *
   * @param playerTable the player table
   * @param countLabel  the count label
   */
  public static void populateLobby(Table playerTable, Label countLabel) {
    //TODO make this less dirty.
//                if (Screens.INSTANCE.isUpdatePlayers()) {
    (new PopulatePlayers(playerTable, countLabel)).start();
//                }
    //TODO get client names from server and populate labels, inc player count label
//        int maxClients = 10;
//        for (int i = 0; i < maxClients; i++) {
//            playerTable.add(createLabel("<space>"));
//            playerTable.row();
//        }
  }

  /**
   * Create label label.
   *
   * @param text the text
   * @return the label
   */
  public static Label createLabel(String text) {
    return new Label(text, Styles.INSTANCE.getLabelStyle());
  }

  /**
   * Create game label label.
   *
   * @param text the text
   * @return the label
   */
  public static Label createGameLabel(String text) {
    return new Label(text, Styles.INSTANCE.getGameLabelStyle());
  }

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
          var temp = Screens.INSTANCE.getScreen(dstScreen);
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
          Sound tank = Gdx.audio.newSound(Gdx.files.internal("audio/Humvee-Stephan_Schutze-1064024548.mp3"));
          tank.play();
        } catch (Exception e) {
          e.printStackTrace();
        }
        return result;
      }
    };
  }
}
