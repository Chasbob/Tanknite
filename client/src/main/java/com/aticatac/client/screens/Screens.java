package com.aticatac.client.screens;

import com.aticatac.client.util.ScreenEnum;
import com.aticatac.client.util.UIFactory;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.HashMap;

public enum Screens {
    INSTANCE;
    private HashMap<Class<?>, AbstractScreen> screens;
    private UIFactory uiFactory;
    private LeaderBoardScreen leaderBoardScreen;
    private GameScreen gameScreen;
    private LobbyScreen lobbyScreen;
    private MainMenuScreen mainMenuScreen;
    private ServerScreen serverScreen;
    private SettingsScreen settingsScreen;
    private SplashScreen splashScreen;

    Screens() {
        screens = new HashMap<>();
        uiFactory = new UIFactory();
//        leaderBoardScreen = new LeaderBoardScreen(ScreenEnum.MAIN_MENU, uiFactory);
//        screens.put(LeaderBoardScreen.class, leaderBoardScreen);
//        screens.put(GameScreen.class, new GameScreen(ScreenEnum.MAIN_MENU, uiFactory));
//        screens.put(LobbyScreen.class, new LobbyScreen(ScreenEnum.MAIN_MENU, uiFactory));
//        gameScreen = new GameScreen(ScreenEnum.MAIN_MENU, uiFactory);
//        lobbyScreen = new LobbyScreen(ScreenEnum.MAIN_MENU, uiFactory);
//        mainMenuScreen = new MainMenuScreen(ScreenEnum.MAIN_MENU, uiFactory);
//        serverScreen = new ServerScreen(ScreenEnum.MAIN_MENU, uiFactory);
//        settingsScreen = new SettingsScreen(ScreenEnum.MAIN_MENU, uiFactory);
//        splashScreen = new SplashScreen(ScreenEnum.MAIN_MENU, uiFactory);
    }

    public <T extends AbstractScreen> void addButtonListener(InputListener listener, TextButton button, Class<T> screen) {
        getScreen(MainMenuScreen.class).getWidgetList().get(0).addListener(listener);
    }

    private <T extends AbstractScreen> T getScreen(Class<T> type) {
        return type.cast(screens.get(type));
    }
}
