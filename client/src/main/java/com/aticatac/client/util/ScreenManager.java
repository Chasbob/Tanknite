package com.aticatac.client.util;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.aticatac.client.screens.AbstractScreen;

/**
 * The type Screen manager.
 */
public class ScreenManager {

    // Singleton: unique instance
    private static ScreenManager instance;

    // Reference to game
    private Game game;

    // Singleton: private constructor
    private ScreenManager() {
        super();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
// Singleton: retrieve instance
    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    /**
     * Initialize.
     *
     * @param game the game
     */
// Initialization with the game class
    public void initialize(Game game) {
        this.game = game;
    }

    /**
     * Show screen.
     *
     * @param targetScreen the target screen
     * @param senderScreen the sender screen
     * @param uiFactory    the ui factory
     */
// Show in the game the screen which enum type is received
    public void showScreen(ScreenEnum targetScreen, ScreenEnum senderScreen, UIFactory uiFactory) {

        // Get current screen to dispose it
        Screen currentScreen = game.getScreen();

        // Show new screen
        AbstractScreen newScreen = targetScreen.getScreen(senderScreen, uiFactory);
        newScreen.buildStage();
        game.setScreen(newScreen);

        // Dispose previous screen
        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }

}
