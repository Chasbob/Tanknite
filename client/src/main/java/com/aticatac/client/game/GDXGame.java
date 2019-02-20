package com.aticatac.client.game;

import com.aticatac.client.networking.Client;
import com.aticatac.client.screens.Screens;
import com.aticatac.common.model.Updates.Update;
import com.badlogic.gdx.Game;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * The type Gdx game.
 */
public class GDXGame extends Game {
    private Client client;
    private BlockingQueue<Update> updates;

    @Override
    public void create() {
        try {
            this.updates = new ArrayBlockingQueue<>(100);
            this.client = new Client();
            //TODO show splash screen whilst it loads
            Screens.INSTANCE.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
}
