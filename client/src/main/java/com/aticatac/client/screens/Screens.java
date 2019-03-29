package com.aticatac.client.screens;

import com.aticatac.client.util.AudioEnum;
import com.aticatac.client.util.Data;
import com.badlogic.gdx.Game;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 * The enum Screens.
 */
public enum Screens {
  /**
   * Instance screens.
   */
  INSTANCE;
  private final Logger logger;
  private final HashMap<Class, AbstractScreen> screens;
  private Game game;
  private Class currentScreen;

  Screens() {
    screens = new HashMap<>();
    screens.put(GameScreen.class, new GameScreen());
    screens.put(LobbyScreen.class, new LobbyScreen());
    screens.put(MainMenuScreen.class, new MainMenuScreen());
    screens.put(ServerScreen.class, new ServerScreen());
    screens.put(GameScreenIsometric.class, new GameScreenIsometric());
    logger = Logger.getLogger(getClass());
  }

  /**
   * Gets current screen.
   *
   * @return the current screen
   */
  public Class getCurrentScreen() {
    return currentScreen;
  }

  /**
   * Initialize.
   *
   * @param game the game
   */
  public void initialize(Game game) {
    AudioEnum.INSTANCE.getMain();
    this.logger.warn("Initializing...");
    Data.INSTANCE.initialise();
    this.game = game;
    this.game.setScreen(getScreen(MainMenuScreen.class));
    this.currentScreen = MainMenuScreen.class;
    for (Class key : screens.keySet()) {
      screens.get(key).buildStage();
    }
    this.logger.warn("End of init");
  }

  /**
   * Gets screen.
   *
   * @param <T>  the type parameter
   * @param type the type
   * @return the screen
   */
  public <T extends AbstractScreen> T getScreen(Class<T> type) {
    return type.cast(screens.get(type));
  }

  /**
   * Show screen.
   *
   * @param type the type
   */
  public void showScreen(Class type) {
    this.logger.info("Going from " + this.currentScreen + " to " + type);
    this.game.setScreen(getScreen(type));
    this.currentScreen = type;
  }

  /**
   * Reload screen.
   *
   * @param <T>  the type parameter
   * @param type the type
   */
  public <T extends AbstractScreen> void reloadScreen(Class<T> type) {
    this.logger.info("reloading " + type + ".");
    getScreen(type).dispose();
    screens.remove(type);
    try {
      screens.put(type, type.getDeclaredConstructor().newInstance());
      screens.get(type).buildStage();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      e.printStackTrace();
    }
//        if (type == LobbyScreen.class) {
//            getScreen(LobbyScreen.class).dispose();
//            screens.remove(LobbyScreen.class);
//            screens.put(LobbyScreen.class, new LobbyScreen());
//            screens.get(LobbyScreen.class).buildStage();
//        }
  }
}
