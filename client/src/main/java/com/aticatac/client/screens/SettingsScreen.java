package com.aticatac.client.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * The type Settings screen.
 */
public class SettingsScreen extends AbstractScreen {
    /**
     * Instantiates a new Settings screen.
     */
    SettingsScreen() {
        super();
    }

    @Override
    public void buildStage() {
        //create root table
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        addActor(rootTable);
        //create table to store back button
        Table backTable = new Table();
        backTable.setFillParent(true);
        rootTable.addActor(backTable);
        backTable.bottom();
        //create back button
        TextButton backButton = UIFactory.createBackButton("back");
        backTable.add(backButton).bottom().padBottom(10);
        backButton.addListener(UIFactory.newChangeScreenEvent(MainMenuScreen.class));
    }
}
