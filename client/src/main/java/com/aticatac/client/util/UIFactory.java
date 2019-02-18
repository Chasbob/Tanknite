package com.aticatac.client.util;

import com.aticatac.client.networking.Client;
import com.aticatac.client.networking.Servers;
import com.aticatac.client.screens.Screens;
import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.ServerInformation;
import com.aticatac.server.networking.Server;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import java.io.IOException;

/**
 * The type Ui factory.
 */
public class UIFactory {
    private static final Styles styles = Styles.getInstance();
    //    private static final ConcurrentHashMap<String, ServerInformation> servers;
    private static final Servers s = Servers.getInstance();
    private static Client client;
    private static ServerInformation address;
    private static boolean serverSelected;
    private static TextButton currentServer;
    private static Server server;

    /**
     * Create title label label.
     *
     * @param text the text
     * @return the label
     */
    public static Label createTitleLabel(String text) {
//        return new Label(text, titleStyle);
        return new Label(text, styles.getTitleStyle());
    }

    /**
     * Create back button text button.
     *
     * @param text the text
     * @return the text button
     */
    public static TextButton createBackButton(String text) {
//        return new TextButton(text, backButtonStyle);
        return new TextButton(text, styles.getBackButtonStyle());
    }

    /**
     * Create listener input listener.
     *
     * @param dstScreen    the dst screen
     * @param senderScreen the sender screen
     * @return the input listener
     */
    public static InputListener createListener(final ScreenEnum dstScreen, final ScreenEnum senderScreen) {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x,
                                     float y, int pointer, int button) {
                Screens.INSTANCE.showScreen(dstScreen);
                Screens.INSTANCE.getScreen(dstScreen).setPrevScreen(senderScreen);
//                ScreenManager.getInstance().showScreen(dstScreen, senderScreen);
                return false;
            }
        };
    }

    /**
     * Create button text button.
     *
     * @param text the text
     * @return the text button
     */
    public static TextButton createButton(String text) {
//        return new TextButton(text, buttonStyle);
        return new TextButton(text, styles.getButtonStyle());
    }

    /**
     * Gets servers.
     *
     * @param serversTable the servers table
     */
    public static void getServers(Table serversTable) {
//        (new ListServers(serversTable)).start();
        serverSelected = false;
    }

    /**
     * Create error label label.
     *
     * @param text the text
     * @return the label
     */
    public static Label createErrorLabel(String text) {
//        return new Label(text, hideLabelStyle);
        return new Label(text, styles.getHideLabelStyle());
    }

    /**
     * Create text field text field.
     *
     * @param text the text
     * @return the text field
     */
    public static TextField createTextField(String text) {
//        return new TextField(text, textFieldStyle);
        return new TextField(text, styles.getTextFieldStyle());
    }

    /**
     * Create start button text button.
     *
     * @param text the text
     * @return the text button
     */
    public static TextButton createStartButton(String text) {
//        return new TextButton(text, startButtonStyle);
        return new TextButton(text, styles.getStartButtonStyle());
    }

    /**
     * Submit button input listener.
     *
     * @param dstScreen    the dst screen
     * @param senderScreen the sender screen
     * @param label        the label
     * @param textField    the text field
     * @return the input listener
     */
    public static InputListener enterLobby(final ScreenEnum dstScreen, final ScreenEnum senderScreen, Label label, TextField textField) {
        return enterLobbyHelper(dstScreen, senderScreen, label, textField);
    }

    private static InputListener enterLobbyHelper(final ScreenEnum dstScreen, final ScreenEnum senderScreen, Label label, TextField textField) {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                String name = textField.getText();
                boolean accepted;
                try {
                    accepted = client.connect(s.getServers().get(0), name);
                    //if name is not taken load game screen, else keep listening
                    if (accepted) {
                        //set the error label to invisible
//                        label.setStyle(uiFactory.hideLabelStyle);
                        label.setStyle(Styles.INSTANCE.getHideLabelStyle());
                        Screens.INSTANCE.showScreen(dstScreen);
                        Screens.INSTANCE.getScreen(dstScreen).setPrevScreen(senderScreen);
                    } else {
//                        label.setStyle(uiFactory.errorStyle);
                        label.setStyle(Styles.INSTANCE.getErrorStyle());
                    }
                    return false;
                } catch (IOException | InvalidBytes e) {
                    e.printStackTrace();
                    //TODO reporting?
                    return false;
                }
            }
        };
    }

    /**
     * Populate lobby.
     *
     * @param playerTable the player table
     * @param countLabel  the count label
     */
    public static void populateLobby(Table playerTable, Label countLabel) {
        //TODO get client names from server and populate labels, inc player count label
        int maxClients = 10;
        for (int i = 0; i < maxClients; i++) {
            playerTable.add(createLabel("<space>"));
            playerTable.row();
        }
    }

    /**
     * Create label label.
     *
     * @param text the text
     * @return the label
     */
    public static Label createLabel(String text) {
//        return new Label(text, labelStyle);
        return new Label(text, styles.getLabelStyle());
    }

    /**
     * Create server button listener input listener.
     *
     * @param serverButton the server button
     * @return the input listener
     */
    static InputListener createServerButtonListener(TextButton serverButton) {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!serverSelected) {
                    serverSelected = true;
                } else {
                    if (currentServer != null) {
//                        currentServer.setStyle(buttonStyle);
                        currentServer.setStyle(styles.getButtonStyle());
                    }
                }
//                serverButton.setStyle(selectedButtonStyle);
                serverButton.setStyle(styles.getSelectedButtonStyle());
                currentServer = serverButton;
                return false;
            }
        };
    }

    /**
     * Create join server listener input listener.
     *
     * @param dstScreen    the dst screen
     * @param senderScreen the sender screen
     * @return the input listener
     */
    public static InputListener createJoinServerListener(final ScreenEnum dstScreen, final ScreenEnum senderScreen) {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (serverSelected) {
                    //TODO show lobby of currentServer
                    Screens.INSTANCE.showScreen(dstScreen);
                    Screens.INSTANCE.getScreen(dstScreen).setPrevScreen(senderScreen);
                }
//                currentServer.setStyle(selectedButtonStyle);
                currentServer.setStyle(styles.getSelectedButtonStyle());
                return false;
            }
        };
    }

    /**
     * Create disconnect listener input listener.
     *
     * @param dstScreen    the dst screen
     * @param senderScreen the sender screen
     * @return the input listener
     */
    public static InputListener createDisconnectListener(final ScreenEnum dstScreen, final ScreenEnum senderScreen) {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //TODO disconnect from current server
                Screens.INSTANCE.showScreen(dstScreen);
                Screens.INSTANCE.getScreen(dstScreen).setPrevScreen(senderScreen);
                return false;
            }
        };
    }

    /**
     * Create host server listener input listener.
     *
     * @param dstScreen    the dst screen
     * @param senderScreen the sender screen
     * @return the input listener
     */
    public static InputListener createHostServerListener(ScreenEnum dstScreen, ScreenEnum senderScreen) {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                server.start();
                Screens.INSTANCE.showScreen(dstScreen);
                Screens.INSTANCE.getScreen(dstScreen).setPrevScreen(senderScreen);
                return false;
            }
        };
    }
}
