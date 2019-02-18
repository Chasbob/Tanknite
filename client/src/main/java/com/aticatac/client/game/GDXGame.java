package com.aticatac.client.game;

import com.aticatac.client.networking.Client;
import com.aticatac.client.util.ScreenEnum;
import com.aticatac.client.util.ScreenManager;
import com.aticatac.client.util.UIFactory;

/**
 * The type Gdx game.
 */
public class GDXGame extends com.badlogic.gdx.Game {

    private Client client;

    @Override
    public void create() {
        this.client = new Client();
        try {
            //TODO show splash screen whilst it loads
            UIFactory uiFactory = new UIFactory(this.client);
            ScreenManager.getInstance().initialize(this);
            ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU, ScreenEnum.MAIN_MENU, uiFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void dispose() {
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
}
