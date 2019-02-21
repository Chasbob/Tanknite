package com.aticatac.common.model.Updates;

import com.aticatac.common.model.Model;
import com.aticatac.common.objectsystem.Container;

import java.util.ArrayList;

/**
 * The type Update.
 */
public class Update extends Model {
    private final boolean changed;
    /**
     * The Players.
     */
    ArrayList<String> players;
    private boolean objectChanged;
    private ArrayList<Container> obj;
    private boolean playersChanged;

    /**
     * Instantiates a new Model.
     *
     * @param changed the changed
     */
    public Update(boolean changed) {
        super("update");
        this.changed = changed;
        this.players = new ArrayList<>();
        this.playersChanged = true;
    }

    public ArrayList<Container> getObj() {
        return obj;
    }

    public void setObj(ArrayList<Container> obj) {
        this.obj = obj;
        this.objectChanged = true;
    }

    /**
     * Gets players.
     *
     * @return the players
     */
    public ArrayList<String> getPlayers() {
        return players;
    }

    /**
     * Add player.
     *
     * @param name the name
     */
    public void addPlayer(String name) {
        this.players.add(name);
    }

    /**
     * Is players changed boolean.
     *
     * @return the boolean
     */
    public boolean isPlayersChanged() {
        return playersChanged;
    }

    /**
     * Is changed boolean.
     *
     * @return the boolean
     */
    public boolean isChanged() {
        return changed;
    }

    @Override
    public String toString() {
        String toString = "Update{" +
                "changed=" + changed +
                ", obj=" + obj.toString() +
                ", players=" + players.toString() +
                ", playersChanged=" + playersChanged +
                '}';
        return toString;
    }
}
