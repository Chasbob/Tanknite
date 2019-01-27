package com.aticatac.model;

/**
 * The Class Model is designed to be a consistent model
 * to allow communication between client and server.
 * <p>
 * It is made serializable to allow transmission over a socket.
 *
 * @author Charles de Freitas
 */
public class Model implements java.io.Serializable {
    /**
     * Used by the server as a unique identifier
     * as to which client it is intended for.
     */
    private final String id;
    private Player player;
    private Map map;

    /**
     * Construct a new Model for a client to send to the server
     * when initiating communication
     *
     * @param id Used to differentiate models on the server-side
     */
    public Model(String id) {
        this.id = id;
    }

    /**
     * Gets map.
     *
     * @return the map
     */
    public Map getMap() {
        return map;
    }

    /**
     * Sets map.
     *
     * @param map the map
     */
    public void setMap(Map map) {
        this.map = map;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets player.
     *
     * @param player the player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Gets id.
     *
     * @return id id
     */
    public String getId() {
        return id;
    }
}
