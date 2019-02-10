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

        //create ui factory instance
        UIFactory ui = new UIFactory();

        //create root table
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        addActor(rootTable);

        //add name label to root
        Label screenTitle = ui.createTitleLabel("Name");
        screenTitle.setFillParent(true);
        rootTable.add(screenTitle).padTop(50).top();

        //create table for label, text field, submit button
        Table usernameTable = new Table();
        usernameTable.setFillParent(true);
        rootTable.addActor(usernameTable);
        usernameTable.defaults().pad(10).width(200).center();

        //create text field
        TextField textField = ui.createTextField("Enter name here");
        usernameTable.add(textField);
        usernameTable.row();

        //create button for submit
        TextButton joinButton = ui.createButton("submit");
        joinButton.addListener(ui.createListener(ScreenEnum.GAME));
        usernameTable.add(joinButton);

    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
