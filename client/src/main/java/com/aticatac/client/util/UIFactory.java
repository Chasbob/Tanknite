package com.aticatac.client.util;

import com.aticatac.client.networking.Client;
import com.aticatac.client.networking.Servers;
import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.ServerInformation;
import com.aticatac.server.networking.Server;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Ui factory.
 */
public class UIFactory {
    private final ConcurrentHashMap<String, ServerInformation> servers;
    private final Servers s = Servers.getInstance();
    private Label.LabelStyle errorStyle;
    private Label.LabelStyle hideLabelStyle;
    private Label.LabelStyle labelStyle;
    private Label.LabelStyle titleStyle;
    private TextButton.TextButtonStyle buttonStyle;
    private TextButton.TextButtonStyle selectedButtonStyle;
    private TextButton.TextButtonStyle startButtonStyle;
    private TextButton.TextButtonStyle backButtonStyle;
    private TextField.TextFieldStyle textFieldStyle;
    private Client client;
    private ServerInformation address;
    private boolean serverSelected;
    private TextButton currentServer;

    /**
     * Instantiates a new Ui factory.
     *
     * @param client the client
     */
    public UIFactory(Client client) throws Exception {
        this.client = client;
        this.loadStyles();
//        this.address = s.getServers().get(0);
        this.servers = new ConcurrentHashMap<>();
        this.server = new Server();
        server.start();
    }

    private void loadStyles() {
        //load in font for menu
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("styles/barcadebrawl.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter15 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter40 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter15.size = 15;
        parameter40.size = 40;
        BitmapFont buttonFont = generator.generateFont(parameter15);
        BitmapFont titleFont = generator.generateFont(parameter40);
        generator.dispose();
        //create title label style
        titleStyle = new Label.LabelStyle();
        titleStyle.font = titleFont;
        titleStyle.fontColor = Color.FOREST;
        //create label style
        labelStyle = new Label.LabelStyle();
        labelStyle.font = buttonFont;
        labelStyle.fontColor = Color.WHITE;
        //create error label style
        errorStyle = new Label.LabelStyle();
        errorStyle.font = buttonFont;
        errorStyle.fontColor = Color.RED;
        //create style to hide error label - set to black
        hideLabelStyle = new Label.LabelStyle();
        hideLabelStyle.font = buttonFont;
        hideLabelStyle.fontColor = Color.BLACK;
        //create text field style with cursor
        textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = buttonFont;
        textFieldStyle.fontColor = Color.WHITE;
        Label.LabelStyle cursorStyle = new Label.LabelStyle();
        cursorStyle.font = buttonFont;
        Label cursorImage = new Label("|", cursorStyle);
        Pixmap cursorColor = new Pixmap((int) cursorImage.getWidth(),
                (int) cursorImage.getHeight(),
                Pixmap.Format.RGB888);
        cursorColor.setColor(Color.FOREST);
        cursorColor.fill();
        textFieldStyle.cursor = new Image(new Texture(cursorColor)).getDrawable();
        Pixmap textFieldColour = new Pixmap(150, 15, Pixmap.Format.RGB888);
        textFieldColour.setColor(Color.DARK_GRAY);
        textFieldColour.fill();
        textFieldStyle.background = new Image(new Texture(textFieldColour)).getDrawable();
        //create a style for buttons
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = buttonFont;
        //create a style for selected buttons
        selectedButtonStyle = new TextButton.TextButtonStyle();
        selectedButtonStyle.font = buttonFont;
        selectedButtonStyle.fontColor = Color.GRAY;
        //create a style for start buttons
        startButtonStyle = new TextButton.TextButtonStyle();
        startButtonStyle.font = buttonFont;
        startButtonStyle.fontColor = Color.FOREST;
        //create style for back buttons
        backButtonStyle = new TextButton.TextButtonStyle();
        backButtonStyle.font = buttonFont;
        backButtonStyle.fontColor = Color.YELLOW;
    }

    /**
     * Create title label label.
     *
     * @param text the text
     * @return the label
     */
    public Label createTitleLabel(String text) {
        return new Label(text, titleStyle);
    }

    /**
     * Create error label label.
     *
     * @param text the text
     * @return the label
     */
    public Label createErrorLabel(String text) {
        return new Label(text, hideLabelStyle);
    }

    /**
     * Create text field text field.
     *
     * @param text the text
     * @return the text field
     */
    public TextField createTextField(String text) {
        return new TextField(text, textFieldStyle);
    }

    public TextButton createStartButton(String text) {
        return new TextButton(text, startButtonStyle);
    }

    /**
     * Create back button text button.
     *
     * @param text the text
     * @return the text button
     */
    public TextButton createBackButton(String text) {
        return new TextButton(text, backButtonStyle);
    }

    /**
     * Create listener input listener.
     *
     * @param dstScreen    the dst screen
     * @param senderScreen the sender screen
     * @param uiFactory    the ui factory
     * @return the input listener
     */
    public InputListener createListener(final ScreenEnum dstScreen, final ScreenEnum senderScreen, final UIFactory uiFactory) {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x,
                                     float y, int pointer, int button) {
                ScreenManager.getInstance().showScreen(dstScreen, senderScreen, uiFactory);
                return false;
            }
        };
    }

    /**
     * Submit button input listener.
     *
     * @param dstScreen    the dst screen
     * @param senderScreen the sender screen
     * @param uiFactory    the ui factory
     * @param label        the label
     * @param textField    the text field
     * @return the input listener
     */
    public InputListener enterLobby(final ScreenEnum dstScreen, final ScreenEnum senderScreen, final UIFactory uiFactory, Label label, TextField textField) {
        return enterLobbyHelper(dstScreen, senderScreen, uiFactory, label, textField, this.address);
    }

    private InputListener enterLobbyHelper(final ScreenEnum dstScreen, final ScreenEnum senderScreen, final UIFactory uiFactory, Label label, TextField textField, ServerInformation information) {
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
                        label.setStyle(uiFactory.hideLabelStyle);
                        ScreenManager.getInstance().showScreen(dstScreen, senderScreen, uiFactory);
                    } else {
                        label.setStyle(uiFactory.errorStyle);
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

    public void populateLobby(Table playerTable, Label countLabel) {
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
    public Label createLabel(String text) {
        return new Label(text, labelStyle);
    }

    public void getServers(Table serversTable, UIFactory uiFactory) {
        (new ListServers(serversTable, uiFactory)).start();
        serverSelected = false;
        //TODO get all servers that are open on the network
//        int maxServers = 10;
//        for (int i = 0; i < maxServers; i++) {
//            TextButton serverButton = createButton("<space>");
//            serverButton.getLabel().setAlignment(Align.left);
//            serversTable.add(serverButton);
//            serverButton.addListener(createServerButtonListener(serverButton));
//            serversTable.row();
//        }
    }

    /**
     * Create button text button.
     *
     * @param text the text
     * @return the text button
     */
    public TextButton createButton(String text) {
        return new TextButton(text, buttonStyle);
    }

    InputListener createServerButtonListener(TextButton serverButton) {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!serverSelected) {
                    serverSelected = true;
                } else {
                    if (currentServer != null) {
                        currentServer.setStyle(buttonStyle);
                    }
                }
                serverButton.setStyle(selectedButtonStyle);
                currentServer = serverButton;
                return false;
            }
        };
    }

    public InputListener createJoinServerListener(final ScreenEnum dstScreen, final ScreenEnum senderScreen, final UIFactory uiFactory) {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (serverSelected) {
                    //TODO show lobby of currentServer
                    ScreenManager.getInstance().showScreen(dstScreen, senderScreen, uiFactory);
                }
                currentServer.setStyle(selectedButtonStyle);
                return false;
            }
        };
    }

    public InputListener createDisconnectListener(final ScreenEnum dstScreen, final ScreenEnum senderScreen, final UIFactory uiFactory) {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //TODO disconnect from current server
                ScreenManager.getInstance().showScreen(dstScreen, senderScreen, uiFactory);
                return false;
            }
        };
    }

    public InputListener createHostServerListener(ScreenEnum dstScreen, ScreenEnum senderScreen, UIFactory uiFactory) {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                ScreenManager.getInstance().showScreen(dstScreen, senderScreen, uiFactory);
                return false;
            }
        };
    }
}
