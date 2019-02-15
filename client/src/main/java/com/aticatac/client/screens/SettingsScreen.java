package com.aticatac.client.screens;

import com.aticatac.client.util.ScreenEnum;
import com.aticatac.client.util.UIFactory;

/**
 * The type Settings screen.
 */
public class SettingsScreen extends AbstractScreen {

    private UIFactory uiFactory;
    private ScreenEnum prevScreen;

    /**
     * Instantiates a new Settings screen.
     */
    public SettingsScreen(ScreenEnum prevScreen, UIFactory uiFactory) {
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
