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
    /**
     * Instantiates a new Username screen.
     */
    public UsernameScreen() {
        super();
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
        Label guidanceLabel = UIFactory.createLabel("Enter username");
        usernameTable.add(guidanceLabel);
        usernameTable.row();
        //create error label
        Label nameTakenLabel = UIFactory.createErrorLabel("Name Taken");
        usernameTable.add(nameTakenLabel);
        usernameTable.row();
        //create text field
        TextField textField = UIFactory.createTextField("");
        usernameTable.add(textField);
        //create button for submit
        TextButton submitButton = UIFactory.createButton("Submit");
        usernameTable.add(submitButton);
        //create custom listener for submit button to get text field text
        if (getPrevScreen() == ScreenEnum.MAIN_MENU) {
            submitButton.addListener(UIFactory.enterLobby(ScreenEnum.GAME, ScreenEnum.USERNAME, nameTakenLabel, textField));
        } else if (getPrevScreen() == ScreenEnum.MUlTIPLAYER) {
            submitButton.addListener(UIFactory.enterLobby(ScreenEnum.LOBBY, ScreenEnum.USERNAME, nameTakenLabel, textField));
        } else if (getPrevScreen() == ScreenEnum.SERVERS) {
            submitButton.addListener(UIFactory.enterLobby(ScreenEnum.LOBBY, ScreenEnum.USERNAME, nameTakenLabel, textField));
        }
        //create table to store back button
        Table backTable = new Table();
        backTable.setFillParent(true);
        rootTable.addActor(backTable);
        backTable.bottom();
        //create back button
        TextButton backButton = UIFactory.createBackButton("quit");
        backTable.add(backButton).bottom().padBottom(10);
        backButton.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU, ScreenEnum.USERNAME));
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
