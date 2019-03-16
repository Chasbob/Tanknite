package com.aticatac.common.model.Updates;

import com.aticatac.common.model.Model;
import com.aticatac.common.objectsystem.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * The type Update.
 */
public class Update extends Model {
  private final boolean changed;
  private boolean start;
  /**
   * The Players.
   */
  private LinkedHashMap<String, Container> players;
  private ArrayList<Container> projectiles;
  //    private boolean objectChanged;
//  private Container rootContainer;
  private boolean playersChanged;

  /**
   * Instantiates a new Model.
   *
   * @param changed the changed
   */
  public Update(boolean changed) {
    super("update");
    this.changed = changed;
    this.players = new LinkedHashMap<>();
    this.playersChanged = true;
    this.start = false;
    this.projectiles = new ArrayList<>();
  }

  /**
   * Clear players.
   */
  public void clearPlayers() {
    this.players.clear();
  }

  /**
   * Clear projectiles.
   */
  public void clearProjectiles() {
    this.projectiles.clear();
  }

  /**
   * Is start boolean.
   *
   * @return the boolean
   */
  public boolean isStart() {
    return start;
  }

  /**
   * Sets start.
   *
   * @param start the start
   */
  public void setStart(boolean start) {
    this.start = start;
  }

  /**
   * Gets i.
   *
   * @param i the
   *
   * @return the i
   */
  public Container getI(int i) {
    return this.players.get(this.players.keySet().toArray()[i]);
  }

  /**
   * Gets me.
   *
   * @param id the id
   *
   * @return the me
   */
  public Container getMe(String id) {
    return players.getOrDefault(id, null);
  }

  /**
   * Gets players.
   *
   * @return the players
   */
  public HashMap<String, Container> getPlayers() {
    return players;
  }

  /**
   * Add player.
   *
   * @param c the c
   */
  public void addPlayer(Container c) {
    this.players.put(c.getId(), c);
  }

  /**
   * Add projectile.
   *
   * @param c the c
   */
  public void addProjectile(Container c) {
    this.projectiles.add(c);
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
        ", players=" + players.toString() +
        ", playersChanged=" + playersChanged +
        '}';
    return toString;
  }

  public ArrayList<Container> getProjectiles() {
    return projectiles;
  }
}
