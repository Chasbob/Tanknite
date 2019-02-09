package com.aticatac.client.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.aticatac.client.util.ScreenEnum;
import com.aticatac.client.util.UIFactory;

public class MultiplayerScreen extends AbstractScreen {

    public MultiplayerScreen() {
        super();
    }

    @Override
    public void buildStage() {

        //create ui factory instance
        UIFactory ui = new UIFactory();

        //create root table
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        addActor(rootTable);

        //add multiplayer label to root
        Label screenTitle = ui.createLabel("Multi Player");
        screenTitle.setFillParent(true);
        rootTable.add(screenTitle).padTop(50).top();

        //create table to store host/join buttons
        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        rootTable.addActor(buttonTable);
        buttonTable.defaults().pad(10).width(100).center();

        //create button for hosting game
        TextButton hostButton = ui.createButton("Host");
        hostButton.addListener(ui.createListener(ScreenEnum.GAME));
        buttonTable.add(hostButton);

        //create button for joining
        TextButton joinButton = ui.createButton("Join");
        joinButton.addListener(ui.createListener(ScreenEnum.GAME));
        buttonTable.add(joinButton);

    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
