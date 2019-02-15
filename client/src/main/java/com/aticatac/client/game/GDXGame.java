package com.aticatac.client.game;

import com.aticatac.client.util.ScreenEnum;
import com.aticatac.client.util.ScreenManager;
import com.aticatac.client.util.UIFactory;

/**
 * The type Gdx game.
 */
public class GDXGame extends com.badlogic.gdx.Game {

    @Override
    public void create() {
        UIFactory uiFactory = new UIFactory();
        ScreenManager.getInstance().initialize(this);
        ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU, ScreenEnum.MAIN_MENU, uiFactory);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }
}
