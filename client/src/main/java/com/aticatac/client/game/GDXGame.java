package com.aticatac.client.game;

import com.aticatac.client.networking.Client;
import com.aticatac.client.screens.Screens;
import com.aticatac.client.util.ScreenEnum;
import com.aticatac.common.model.Updates.Update;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * The type Gdx game.
 */
public class GDXGame extends com.badlogic.gdx.Game {
    private Client client;
    private BlockingQueue<Update> updates;

    @Override
    public void create() {
        try {
            this.updates = new ArrayBlockingQueue<>(100);
            this.client = new Client(updates);
            //TODO show splash screen whilst it loads
//            ScreenManager.getInstance().initialize(this);
//            ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU, ScreenEnum.MAIN_MENU);
            Screens s = Screens.INSTANCE;
            s.initialize(this);
//            s.showScreen(ScreenEnum.MAIN_MENU);
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
