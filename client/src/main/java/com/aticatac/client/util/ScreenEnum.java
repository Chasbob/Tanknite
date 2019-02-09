package com.aticatac.client.util;

import com.aticatac.client.screens.*;

public enum ScreenEnum {

    SPLASH {
        public AbstractScreen getScreen(Object... params) {
            return new SplashScreen();
        }
    },

    MAIN_MENU {
        public AbstractScreen getScreen(Object... params) {
            return new MainMenuScreen();
        }
    },

    MUlTIPLAYER {
        public AbstractScreen getScreen(Object... params) {
            return new MultiplayerScreen();
        }
    },

    GAME {
        public AbstractScreen getScreen(Object... params) {
            return new GameScreen();
        }
    },

    LEADERBOARD {
        public AbstractScreen getScreen(Object... params) {
            return new LeaderBoardScreen();
        }
    },

    SETTINGS {
        public AbstractScreen getScreen(Object... params) {
            return new SettingsScreen();
        }
    },

    USERNAME {
        public AbstractScreen getScreen(Object... params) {
            return new UsernameScreen();
        }
    };

    public abstract AbstractScreen getScreen(Object... params);
}