package com.aticatac.client.screens;

import com.aticatac.client.util.Styles;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * The type Server screen.
 */
public class ServerScreen extends AbstractScreen {
    private Boolean serverSelected;
    private TextButton currentServer;
    private final SpriteBatch batch;

    /**
     * Instantiates a new Server screen.
     */
    ServerScreen() {
        super();
        serverSelected = false;
        //TODO dont use null
        currentServer = null;
        batch = new SpriteBatch();
    }

    /**
     * Gets current server.
     *
     * @return the current server
     */
    public TextButton getCurrentServer() {
        return currentServer;
    }

    /**
     * Sets current server.
     *
     * @param currentServer the current server
     */
    public void setCurrentServer(TextButton currentServer) {
        this.currentServer = currentServer;
    }

    /**
     * Gets server selected.
     *
     * @return the server selected
     */
    public Boolean getServerSelected() {
        return serverSelected;
    }

    /**
     * Sets server selected.
     *
     * @param serverSelected the server selected
     */
    public void setServerSelected(Boolean serverSelected) {
        this.serverSelected = serverSelected;
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
        //create table for top lables and refresh button
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
        joinButton.addListener(UIFactory.newListenerEvent(() -> {
                    if (serverSelected) {
                        //TODO show lobby of currentServer
                        Screens.INSTANCE.showScreen(UsernameScreen.class);
                    }
                    if (currentServer!=null){
                        currentServer.setStyle(Styles.INSTANCE.getSelectedButtonStyle());
                    }
                    return false;
                }
        ));
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
        backButton.addListener(UIFactory.newChangeScreenEvent(MainMenuScreen.class));
        //add refresh button
        ImageButton refreshButton = Styles.getInstance().getRefreshButton();
        serverDetailsTable.add(refreshButton).right().padLeft(100);
        refreshButton.addListener(UIFactory.newListenerEvent(()->{
                    UIFactory.getServers(serversTable);
                    return false;
                }
        ));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();

        batch.end();
    }
}
