package com.aticatac.client.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.aticatac.client.util.ScreenEnum;
import com.aticatac.client.util.UIFactory;

/**
 * The type Multiplayer screen.
 */
public class MultiplayerScreen extends AbstractScreen {

    private ScreenEnum prevScreen;
    private UIFactory uiFactory;

    /**
     * Instantiates a new Multiplayer screen.
     */
    public MultiplayerScreen(ScreenEnum prevScreen, UIFactory uiFactory) {
        super();
        this.prevScreen = prevScreen;
        this.uiFactory = uiFactory;
    }

    @Override
    public void buildStage() {

        //create root table
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        addActor(rootTable);

        //add multiplayer label to root
        Label screenTitle = uiFactory.createTitleLabel("Multi Player");
        screenTitle.setFillParent(true);
        rootTable.add(screenTitle).padTop(50).top();

        //create table to store host/join buttons
        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        rootTable.addActor(buttonTable);
        buttonTable.defaults().pad(10).width(100).center();

        //create button for hosting game
        TextButton hostButton = uiFactory.createButton("Host");
        hostButton.addListener(uiFactory.createListener(ScreenEnum.USERNAME, ScreenEnum.MUlTIPLAYER, uiFactory));
        buttonTable.add(hostButton);

        //create button for joining
        TextButton joinButton = uiFactory.createButton("Join");
        joinButton.addListener(uiFactory.createListener(ScreenEnum.USERNAME, ScreenEnum.MUlTIPLAYER, uiFactory));
        buttonTable.add(joinButton);

        //create table to store back button
        Table backTable = new Table();
        backTable.setFillParent(true);
        rootTable.addActor(backTable);
        backTable.bottom();

        //create back button
        TextButton backButton = uiFactory.createBackButton("back");
        backTable.add(backButton).bottom().padBottom(10);
        backButton.addListener(uiFactory.createListener(prevScreen, ScreenEnum.MUlTIPLAYER, uiFactory));

    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
