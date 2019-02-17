package com.aticatac.client.screens;

import com.aticatac.client.util.ScreenEnum;
import com.aticatac.client.util.UIFactory;

public class ServerScreen extends AbstractScreen {

    private UIFactory uiFactory;
    private ScreenEnum prevScreen;

    public ServerScreen(ScreenEnum prevScreen, UIFactory uiFactory) {
        super();
        this.uiFactory = uiFactory;
        this.prevScreen = prevScreen;
    }

    @Override
    public void buildStage() {

    }
}
