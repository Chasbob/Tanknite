package com.aticatac.client.screens;

import com.aticatac.client.util.ScreenEnum;
import com.aticatac.client.util.UIFactory;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ServerScreen extends AbstractScreen {
    private UIFactory uiFactory;
    private ScreenEnum prevScreen;

    public ServerScreen(ScreenEnum prevScreen, UIFactory uiFactory) {
        super();
        this.uiFactory = uiFactory;
        this.prevScreen = prevScreen;
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
        Table serverDetailsTable = new Table();
        serverDetailsTable.setFillParent(true);
        dataTable.addActor(serverDetailsTable);
        serverDetailsTable.top().padTop(50);
        //add labels to serverDetailsTable
        Label waitingLabel = uiFactory.createLabel("Open servers");
        serverDetailsTable.add(waitingLabel);
        //add table with join button to get into lobby after entering username
        Table joinTable = new Table();
        joinTable.setFillParent(true);
        joinTable.top().padTop(100);
        TextButton joinButton = uiFactory.createStartButton("Join");
        joinTable.add(joinButton);
        joinButton.addListener(uiFactory.createJoinServerListener(ScreenEnum.USERNAME, ScreenEnum.SERVERS, uiFactory));
        dataTable.addActor(joinTable);
        //add table to store all current open servers
        Table serversTable = new Table();
        serversTable.setFillParent(true);
        serversTable.defaults().pad(10).width(450);
        serversTable.top().padTop(150);
        uiFactory.getServers(serversTable, this.uiFactory);
        dataTable.addActor(serversTable);
        //create table to store back button
        Table backTable = new Table();
        backTable.setFillParent(true);
        rootTable.addActor(backTable);
        backTable.bottom();
        TextButton backButton = uiFactory.createBackButton("back");
        backTable.add(backButton).bottom().padBottom(10);
        backButton.addListener(uiFactory.createListener(ScreenEnum.MAIN_MENU, ScreenEnum.LOBBY, uiFactory));
    }
}
