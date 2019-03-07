package com.aticatac.client.screens;

import com.badlogic.gdx.Game;
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
  private Class previousScreen;

  Screens() {
    screens = new HashMap<>();
    screens.put(LeaderBoardScreen.class, new LeaderBoardScreen());
    screens.put(GameScreen.class, new GameScreen());
    screens.put(LobbyScreen.class, new LobbyScreen());
    screens.put(MainMenuScreen.class, new MainMenuScreen());
    screens.put(ServerScreen.class, new ServerScreen());
    screens.put(SettingsScreen.class, new SettingsScreen());
    screens.put(MultiplayerScreen.class, new MultiplayerScreen());
    screens.put(SplashScreen.class, new SplashScreen());
    screens.put(UsernameScreen.class, new UsernameScreen());
    logger = Logger.getLogger(getClass());
  }

  /**
   * Gets previous screen.
   *
   * @return the previous screen
   */
  public Class getPreviousScreen() {
    return previousScreen;
  }

  /**
   * Initialize.
   *
   * @param game the game
   */
// Initialization with the game class
  public void initialize(Game game) {
    this.logger.warn("Initializing...");
    this.game = game;
//        showScreen(MainMenuScreen.class);
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
    this.previousScreen = this.currentScreen;
    this.game.setScreen(getScreen(type));
    this.currentScreen = type;
  }
}
