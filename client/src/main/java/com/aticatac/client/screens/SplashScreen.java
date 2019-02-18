package com.aticatac.client.screens;

import com.aticatac.client.util.ScreenEnum;
import com.aticatac.client.util.UIFactory;

/**
 * The type Splash screen.
 */
public class SplashScreen extends AbstractScreen {

    private ScreenEnum prevScreen;
    private UIFactory uiFactory;

    /**
     * Instantiates a new Splash screen.
     */
    public SplashScreen(ScreenEnum prevScreen, UIFactory uiFactory) {
        super();
        this.prevScreen = prevScreen;
        this.uiFactory = uiFactory;
    }

    @Override
    public void buildStage() {

    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
