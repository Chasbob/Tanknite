package com.aticatac.client.util;

import com.aticatac.client.screens.*;

/**
 * The enum Screen enum.
 */
public enum ScreenEnum {

    /**
     * The Splash.
     */
    SPLASH {
        public AbstractScreen getScreen(ScreenEnum prevScreen) {
            return new SplashScreen(prevScreen);
        }
    },

    /**
     * The Main menu.
     */
    MAIN_MENU {
        public AbstractScreen getScreen(ScreenEnum prevScreen) {
            return new MainMenuScreen(prevScreen);
        }
    },

    /**
     * The M ul tiplayer.
     */
    MUlTIPLAYER {
        public AbstractScreen getScreen(ScreenEnum prevScreen) {
            return new MultiplayerScreen(prevScreen);
        }
    },

    /**
     * The Game.
     */
    GAME {
        public AbstractScreen getScreen(ScreenEnum prevScreen) {
            return new GameScreen(prevScreen);
        }
    },

    /**
     * The Leaderboard.
     */
    LEADERBOARD {
        public AbstractScreen getScreen(ScreenEnum prevScreen) {
            return new LeaderBoardScreen(prevScreen);
        }
    },

    /**
     * The Settings.
     */
    SETTINGS {
        public AbstractScreen getScreen(ScreenEnum prevScreen) {
            return new SettingsScreen(prevScreen);
        }
    },

    /**
     * The Username.
     */
    USERNAME {
        public AbstractScreen getScreen(ScreenEnum prevScreen) {
            return new UsernameScreen(prevScreen);
        }
    },

    LOBBY {
        public AbstractScreen getScreen(ScreenEnum prevScreen) {
            return new LobbyScreen(prevScreen);
        }
    },

    SERVERS {
        public AbstractScreen getScreen(ScreenEnum prevScreen) {
            return new ServerScreen(prevScreen);
        }
    };

    /**
     * Gets screen.
     *
     * @param ui the UIFactory instance
     * @return the screen
     */
    public abstract AbstractScreen getScreen(ScreenEnum prevScreen);
}