package com.aticatac.client.screens;

import com.aticatac.client.util.ScreenEnum;
import com.aticatac.client.util.ScreenManager;
import com.aticatac.client.util.UIFactory;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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
        usernameTable.defaults().pad(10).width(150).center();

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
        submitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                boolean taken = false;
                String name = textField.getText();
                //TODO send message to server to see if name has been taken
                //if name is not taken load game screen, else keep listening
                if (!taken) {
                    //set the error label to invisible
                    nameTakenLabel.setStyle(uiFactory.hideErrorStyle);
                    ScreenManager.getInstance().showScreen(ScreenEnum.GAME, ScreenEnum.USERNAME, uiFactory);
                } else {
                    nameTakenLabel.setStyle(uiFactory.errorStyle);
                }
                return false;
            }
        });

        //create table to store back button
        Table backTable = new Table();
        backTable.setFillParent(true);
        rootTable.addActor(backTable);
        backTable.bottom();

        //create back button
        TextButton backButton = uiFactory.createBackButton("back");
        backTable.add(backButton).bottom().padBottom(10);
        backButton.addListener(uiFactory.createListener(prevScreen, ScreenEnum.USERNAME, uiFactory));
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
