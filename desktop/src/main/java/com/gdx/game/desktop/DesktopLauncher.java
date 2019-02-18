package com.gdx.game.desktop;

import com.aticatac.client.game.GDXGame;
import com.aticatac.client.screens.MainMenuScreen;
import com.aticatac.client.screens.Screens;
import com.aticatac.client.util.Styles;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new GDXGame(), config);
        Screens s = Screens.INSTANCE;
        s.addButtonListener(new InputListener(), new TextButton("", Styles.INSTANCE.getButtonStyle()), MainMenuScreen.class);
        config.width = 640;
        config.height = 640;
    }
}
