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
        public AbstractScreen getScreen(Object... params) {
            return new SplashScreen();
        }
    },

    /**
     * The Main menu.
     */
    MAIN_MENU {
        public AbstractScreen getScreen(Object... params) {
            return new MainMenuScreen();
        }
    },

    /**
     * The M ul tiplayer.
     */
    MUlTIPLAYER {
        public AbstractScreen getScreen(Object... params) {
            return new MultiplayerScreen();
        }
    },

    /**
     * The Game.
     */
    GAME {
        public AbstractScreen getScreen(Object... params) {
            return new GameScreen();
        }
    },

    /**
     * The Leaderboard.
     */
    LEADERBOARD {
        public AbstractScreen getScreen(Object... params) {
            return new LeaderBoardScreen();
        }
    },

    /**
     * The Settings.
     */
    SETTINGS {
        public AbstractScreen getScreen(Object... params) {
            return new SettingsScreen();
        }
    },

    /**
     * The Username.
     */
    USERNAME {
        public AbstractScreen getScreen(Object... params) {
            return new UsernameScreen();
        }
    };

    /**
     * Gets screen.
     *
     * @param params the params
     * @return the screen
     */
    public abstract AbstractScreen getScreen(Object... params);
}