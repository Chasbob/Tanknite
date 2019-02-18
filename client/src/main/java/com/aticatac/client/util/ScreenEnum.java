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
        public AbstractScreen getScreen(ScreenEnum prevScreen, UIFactory ui) {
            return new SplashScreen(prevScreen, ui);
        }
    },

    /**
     * The Main menu.
     */
    MAIN_MENU {
        public AbstractScreen getScreen(ScreenEnum prevScreen, UIFactory ui) {
            return new MainMenuScreen(prevScreen, ui);
        }
    },

    /**
     * The M ul tiplayer.
     */
    MUlTIPLAYER {
        public AbstractScreen getScreen(ScreenEnum prevScreen, UIFactory ui) {
            return new MultiplayerScreen(prevScreen, ui);
        }
    },

    /**
     * The Game.
     */
    GAME {
        public AbstractScreen getScreen(ScreenEnum prevScreen, UIFactory ui) {
            return new GameScreen(prevScreen, ui);
        }
    },

    /**
     * The Leaderboard.
     */
    LEADERBOARD {
        public AbstractScreen getScreen(ScreenEnum prevScreen, UIFactory ui) {
            return new LeaderBoardScreen(prevScreen, ui);
        }
    },

    /**
     * The Settings.
     */
    SETTINGS {
        public AbstractScreen getScreen(ScreenEnum prevScreen, UIFactory ui) {
            return new SettingsScreen(prevScreen, ui);
        }
    },

    /**
     * The Username.
     */
    USERNAME {
        public AbstractScreen getScreen(ScreenEnum prevScreen, UIFactory ui) {
            return new UsernameScreen(prevScreen, ui);
        }
    },

    LOBBY {
        public AbstractScreen getScreen(ScreenEnum prevScreen, UIFactory ui) {
            return new LobbyScreen(prevScreen, ui);
        }
    },

    SERVERS {
        public AbstractScreen getScreen(ScreenEnum prevScreen, UIFactory ui) {
            return new ServerScreen(prevScreen, ui);
        }
    };

    /**
     * Gets screen.
     *
     * @param ui the UIFactory instance
     * @return the screen
     */
    public abstract AbstractScreen getScreen(ScreenEnum prevScreen, UIFactory ui);
}