package com.aticatac.client.screens;

import com.aticatac.client.util.ScreenEnum;
import com.aticatac.client.util.UIFactory;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ServerScreen extends AbstractScreen {
    private ScreenEnum prevScreen;

    public ServerScreen(ScreenEnum prevScreen) {
        super();
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
        Label waitingLabel = UIFactory.createLabel("Open servers");
        serverDetailsTable.add(waitingLabel);
        //add table with join button to get into lobby after entering username
        Table joinTable = new Table();
        joinTable.setFillParent(true);
        joinTable.top().padTop(100);
        TextButton joinButton = UIFactory.createStartButton("Join");
        joinTable.add(joinButton);
        joinButton.addListener(UIFactory.createJoinServerListener(ScreenEnum.USERNAME, ScreenEnum.SERVERS));
        dataTable.addActor(joinTable);
        //add table to store all current open servers
        Table serversTable = new Table();
        serversTable.setFillParent(true);
        serversTable.defaults().pad(10).width(450);
        serversTable.top().padTop(150);
        UIFactory.getServers(serversTable);
        dataTable.addActor(serversTable);
        //create table to store back button
        Table backTable = new Table();
        backTable.setFillParent(true);
        rootTable.addActor(backTable);
        backTable.bottom();
        TextButton backButton = UIFactory.createBackButton("back");
        backTable.add(backButton).bottom().padBottom(10);
        backButton.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU, ScreenEnum.LOBBY));
    }
}
