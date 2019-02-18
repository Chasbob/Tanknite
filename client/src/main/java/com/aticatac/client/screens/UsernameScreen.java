package com.aticatac.client.screens;

import com.aticatac.client.util.ScreenEnum;
import com.aticatac.client.util.UIFactory;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 * The type Username screen.
 */
public class UsernameScreen extends AbstractScreen {
    private UIFactory uiFactory;
    private ScreenEnum prevScreen;

    /**
     * Instantiates a new Username screen.
     */
    public UsernameScreen(ScreenEnum prevScreen, UIFactory uiFactory) {
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
        //create table for label, text field, submit button
        Table usernameTable = new Table();
        rootTable.addActor(usernameTable);
        usernameTable.setFillParent(true);
        usernameTable.center();
        usernameTable.defaults().pad(10).width(200).height(30).center();
        //create guidance label
        Label guidanceLabel = uiFactory.createLabel("Enter username");
        usernameTable.add(guidanceLabel);
        usernameTable.row();
        //create error label
        Label nameTakenLabel = uiFactory.createErrorLabel("Name Taken");
        usernameTable.add(nameTakenLabel);
        usernameTable.row();
        //create text field
        TextField textField = uiFactory.createTextField("");
        usernameTable.add(textField);
        //create button for submit
        TextButton submitButton = uiFactory.createButton("Submit");
        usernameTable.add(submitButton);
        //create custom listener for submit button to get text field text
        if (prevScreen == ScreenEnum.MAIN_MENU) {
            submitButton.addListener(uiFactory.enterLobby(ScreenEnum.GAME, ScreenEnum.USERNAME, uiFactory, nameTakenLabel, textField));
        } else if (prevScreen == ScreenEnum.MUlTIPLAYER) {
            submitButton.addListener(uiFactory.enterLobby(ScreenEnum.LOBBY, ScreenEnum.USERNAME, uiFactory, nameTakenLabel, textField));
        } else if (prevScreen == ScreenEnum.SERVERS) {
            submitButton.addListener(uiFactory.enterLobby(ScreenEnum.LOBBY, ScreenEnum.USERNAME, uiFactory, nameTakenLabel, textField));
        }
        //create table to store back button
        Table backTable = new Table();
        backTable.setFillParent(true);
        rootTable.addActor(backTable);
        backTable.bottom();
        //create back button
        TextButton backButton = uiFactory.createBackButton("quit");
        backTable.add(backButton).bottom().padBottom(10);
        backButton.addListener(uiFactory.createListener(ScreenEnum.MAIN_MENU, ScreenEnum.USERNAME, uiFactory));
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
