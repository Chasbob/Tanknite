package com.aticatac.client.screens;

import com.aticatac.client.networking.Client;
import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.ServerInformation;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.objectsystem.GameObject;
import com.badlogic.gdx.Game;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 * The enum Screens.
 */
public enum Screens {
    /**
     * Instance screens.
     */
    INSTANCE;
    private final Logger logger;
    private final HashMap<Class, AbstractScreen> screens;
    private Update update;
    private ArrayList<String> clients;
    private Game game;
    private boolean isInit;
    private Class currentScreen;
    private Class previousScreen;
    private ServerInformation localhost;
    private ServerInformation currentInformation;
    private boolean singleplayer;
    private boolean updatePlayers;
    private Client client;
    private GameObject root;

    Screens() {
        try {
            //TODO don't hard code the port.
            this.localhost = new ServerInformation("localhost", InetAddress.getLocalHost(), 5500);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        isInit = false;
        screens = new HashMap<>();
        screens.put(LeaderBoardScreen.class, new LeaderBoardScreen());
        screens.put(GameScreen.class, new GameScreen());
        screens.put(LobbyScreen.class, new LobbyScreen());
        screens.put(MainMenuScreen.class, new MainMenuScreen());
        screens.put(ServerScreen.class, new ServerScreen());
        screens.put(SettingsScreen.class, new SettingsScreen());
        screens.put(MultiplayerScreen.class, new MultiplayerScreen());
        screens.put(SplashScreen.class, new SplashScreen());
        screens.put(UsernameScreen.class, new UsernameScreen());
        logger = Logger.getLogger(getClass());
        //this.updates = new ArrayBlockingQueue<>(1024);
        this.update = new Update(true);
        this.clients = new ArrayList<>();
    }

    public void setUpdate(Update update) {
        this.update = update;
        try {
            this.root = new GameObject(this.update.getRootContainer());
        } catch (InvalidClassInstance | ComponentExistsException e) {
            this.logger.error(e);
        }
    }

    public GameObject getRoot() {
        try {
            this.root = new GameObject(this.update.getRootContainer());
        } catch (InvalidClassInstance | ComponentExistsException e) {
            this.logger.error(e);
        }
        return this.root;
    }

    public ArrayList<String> getClients() {
        return clients;
    }

    public boolean isUpdatePlayers() {
        return updatePlayers;
    }

    public void setUpdatePlayers(boolean updatePlayers) {
        this.updatePlayers = updatePlayers;
    }

    /**
     * Gets current information.
     *
     * @return the current information
     */
    public ServerInformation getCurrentInformation() {
        if (this.singleplayer) {
            return this.localhost;
        } else {
            return currentInformation;
        }
    }

    /**
     * Sets current information.
     *
     * @param currentInformation the current information
     */
    public void setCurrentInformation(ServerInformation currentInformation) {
        this.currentInformation = currentInformation;
    }

    /**
     * Gets localhost.
     *
     * @return the localhost
     */
    public ServerInformation getLocalhost() {
        return localhost;
    }

    /**
     * Sets localhost.
     *
     * @param localhost the localhost
     */
    public void setLocalhost(ServerInformation localhost) {
        this.localhost = localhost;
    }

    /**
     * Gets current screen.
     *
     * @return the current screen
     */
    public Class getCurrentScreen() {
        return currentScreen;
    }

    /**
     * Gets previous screen.
     *
     * @return the previous screen
     */
    public Class getPreviousScreen() {
        return previousScreen;
    }

    public Client getClient() {
        return client;
    }

    public boolean connect(String id) throws IOException, InvalidBytes {
        return this.client.connect(this.getCurrentInformation(), id);
    }

    /**
     * Initialize.
     *
     * @param game the game
     */
// Initialization with the game class
    public void initialize(Game game) {
        this.client = new Client();
        this.logger.warn("Initializing...");
        this.game = game;
        this.isInit = true;
        this.game.setScreen(getScreen(MainMenuScreen.class));
        this.currentScreen = MainMenuScreen.class;
        for (Class key : screens.keySet()) {
            screens.get(key).buildStage();
        }
        this.logger.warn("End of init");
    }

    /**
     * Gets screen.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @return the screen
     */
    public <T extends AbstractScreen> T getScreen(Class<T> type) {
        return type.cast(screens.get(type));
    }

    /**
     * Show screen.
     *
     * @param <T>  the type parameter
     * @param type the type
     */
    public <T extends AbstractScreen> void showScreen(Class type) {
        this.logger.info("Going from " + this.currentScreen + " to " + type);
        this.previousScreen = this.currentScreen;
        this.game.setScreen(getScreen(type));
        this.currentScreen = type;
    }

    /**
     * Is singleplayer boolean.
     *
     * @return the boolean
     */
    public boolean isSingleplayer() {
        return singleplayer;
    }

    /**
     * Sets singleplayer.
     *
     * @param singleplayer the singleplayer
     */
    public void setSingleplayer(boolean singleplayer) {
        this.singleplayer = singleplayer;
    }
}
