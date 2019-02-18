package com.aticatac.client.screens;

import com.aticatac.client.util.ScreenEnum;
import com.aticatac.client.util.UIFactory;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * The type Multiplayer screen.
 */
public class MultiplayerScreen extends AbstractScreen {
    /**
     * Instantiates a new Multiplayer screen.
     */
    public MultiplayerScreen() {
        super();
    }

    @Override
    public void buildStage() {
        //create root table
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        addActor(rootTable);
        //add multiplayer label to root
        Label screenTitle = UIFactory.createTitleLabel("Multi Player");
        screenTitle.setFillParent(true);
        rootTable.add(screenTitle).padTop(50).top();
        //create table to store host/join buttons
        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        rootTable.addActor(buttonTable);
        buttonTable.defaults().pad(10).width(100).center();
        //create button for hosting game
        TextButton hostButton = UIFactory.createButton("Host");
        hostButton.addListener(UIFactory.createHostServerListener(ScreenEnum.USERNAME, ScreenEnum.MUlTIPLAYER));
        buttonTable.add(hostButton);
        //create button for joining
        TextButton joinButton = UIFactory.createButton("Join");
        joinButton.addListener(UIFactory.createListener(ScreenEnum.SERVERS, ScreenEnum.MUlTIPLAYER));
        buttonTable.add(joinButton);
        //create table to store back button
        Table backTable = new Table();
        backTable.setFillParent(true);
        rootTable.addActor(backTable);
        backTable.bottom();
        //create back button
        TextButton backButton = UIFactory.createBackButton("back");
        backTable.add(backButton).bottom().padBottom(10);
        backButton.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU, ScreenEnum.MUlTIPLAYER));
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
