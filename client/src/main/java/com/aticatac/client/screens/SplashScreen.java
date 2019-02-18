package com.aticatac.client.screens;

import com.aticatac.client.util.ScreenEnum;

/**
 * The type Splash screen.
 */
public class SplashScreen extends AbstractScreen {
    private ScreenEnum prevScreen;

    /**
     * Instantiates a new Splash screen.
     */
    public SplashScreen(ScreenEnum prevScreen) {
        super();
        this.prevScreen = prevScreen;
    }

    @Override
    public void buildStage() {
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
