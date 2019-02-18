package com.aticatac.client.screens;

import com.aticatac.client.util.ScreenEnum;
import com.aticatac.client.util.UIFactory;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class LobbyScreen extends AbstractScreen {

    private ScreenEnum prevScreen;
    private UIFactory uiFactory;

    public LobbyScreen(ScreenEnum prevScreen, UIFactory uiFactory) {
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
        //create data table
        Table dataTable = new Table();
        dataTable.setFillParent(true);
        rootTable.addActor(dataTable);
        //create table for waiting for players label and player count for the lobby
        Table lobbyDetailsTable = new Table();
        lobbyDetailsTable.setFillParent(true);
        dataTable.addActor(lobbyDetailsTable);
        lobbyDetailsTable.top().padTop(50);
        //add labels to lobbyDetailsTable
        Label waitingLabel = uiFactory.createLabel("Waiting for players..   ");
        lobbyDetailsTable.add(waitingLabel);
        Label countLabel = uiFactory.createLabel("0");
        lobbyDetailsTable.add(countLabel);
        Label maxLabel = uiFactory.createLabel("/10");
        lobbyDetailsTable.add(maxLabel);
        //add table with start button to load game
        Table startTable = new Table();
        startTable.setFillParent(true);
        startTable.top().padTop(100);
        TextButton startButton = uiFactory.createStartButton("Start");
        startTable.add(startButton);
        startButton.addListener(uiFactory.createListener(ScreenEnum.GAME, ScreenEnum.LOBBY, uiFactory));
        dataTable.addActor(startTable);
        //add table to store players joining server
        Table playersTable = new Table();
        playersTable.setFillParent(true);
        playersTable.defaults().pad(10).left().width(450);
        playersTable.top().padTop(150);
        uiFactory.populateLobby(playersTable, countLabel);
        dataTable.addActor(playersTable);
        //create table to store back button
        Table backTable = new Table();
        backTable.setFillParent(true);
        rootTable.addActor(backTable);
        backTable.bottom();
        TextButton backButton = uiFactory.createBackButton("quit");
        backTable.add(backButton).bottom().padBottom(10);
        backButton.addListener(uiFactory.createListener(ScreenEnum.MAIN_MENU, ScreenEnum.LOBBY, uiFactory));
    }
}
