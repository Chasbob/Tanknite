package com.aticatac.client.screens;

import com.aticatac.client.util.ScreenEnum;
import com.badlogic.gdx.Game;

import java.util.HashMap;

/**
 * The enum Screens.
 */
public enum Screens {
    /**
     * Instance screens.
     */
    INSTANCE;
    private final HashMap<ScreenEnum, AbstractScreen> screens;
    private Game game;
    private boolean isInit;

    Screens() {
        isInit = false;
        screens = new HashMap<>();
        screens.put(ScreenEnum.LEADERBOARD, new LeaderBoardScreen());
        screens.put(ScreenEnum.GAME, new GameScreen());
        screens.put(ScreenEnum.LOBBY, new LobbyScreen());
        screens.put(ScreenEnum.MAIN_MENU, new MainMenuScreen());
        screens.put(ScreenEnum.SERVERS, new ServerScreen());
        screens.put(ScreenEnum.SETTINGS, new SettingsScreen());
        screens.put(ScreenEnum.MUlTIPLAYER, new MultiplayerScreen());
        screens.put(ScreenEnum.SPLASH, new SplashScreen());
        screens.put(ScreenEnum.USERNAME, new UsernameScreen());
    }

    /**
     * Initialize.
     *
     * @param game the game
     */
// Initialization with the game class
    public void initialize(Game game) {
        this.game = game;
        this.isInit = true;
        getScreen(ScreenEnum.MAIN_MENU).buildStage();
        this.game.setScreen(getScreen(ScreenEnum.MAIN_MENU));
    }

    /**
     * Gets screen.
     *
     * @param screenEnum the screen enum
     * @return the screen
     */
    public AbstractScreen getScreen(ScreenEnum screenEnum) {
        return screens.get(screenEnum);
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public Screens getInstance() {
        return INSTANCE;
    }

    /**
     * Gets prev screen.
     *
     * @param screenEnum the screen enum
     * @return the prev screen
     */
    public ScreenEnum getPrevScreen(ScreenEnum screenEnum) {
        return getScreen(screenEnum).getPrevScreen();
    }

    /**
     * Show screen.
     *
     * @param screenEnum the screen enum
     */
    public void showScreen(ScreenEnum screenEnum) {
        this.game.getScreen().dispose();
        getScreen(screenEnum).buildStage();
        this.game.setScreen(getScreen(screenEnum));
    }
}
