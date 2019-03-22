package com.aticatac.common.model.Updates;

import com.aticatac.common.model.Model;
import com.aticatac.common.objectsystem.Container;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Update.
 */
public class Update extends Model {
  private final boolean changed;
  /**
   * The Players.
   */
  private final ConcurrentHashMap<String, Container> players;
  private final ConcurrentHashMap<String, Container> projectiles;
  private final ConcurrentHashMap<String, Container> powerups;
  private final ConcurrentHashMap<String, Container> newShots;
  private boolean start;
  //    private boolean objectChanged;
//  private Container rootContainer;
  private boolean playersChanged;

  public ConcurrentHashMap<String, Container> getPowerups() {
    return powerups;
  }

  public ConcurrentHashMap<String, Container> getNewShots() {
    return newShots;
  }

  /**
   * Instantiates a new Model.
   *
   * @param changed the changed
   */
  public Update(boolean changed) {
    super("update");
    this.changed = changed;
    this.players = new ConcurrentHashMap<>();
    this.playersChanged = true;
    this.start = false;
    this.projectiles = new ConcurrentHashMap<>();
    powerups = new ConcurrentHashMap<>();
    newShots = new ConcurrentHashMap<>();
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

  public void clearPowerups() {
    this.powerups.clear();
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
  public ConcurrentHashMap<String, Container> getPlayers() {
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

  public void removePlayer(Container c) {
    this.players.remove(c.getId());
  }

  public void removePowerup(Container c) {
    this.powerups.remove(c.getId());
  }

  public void addPowerup(Container c) {
    this.powerups.put(c.getId(), c);
//    this.powerupmap.put(c.hashCode(), c);
  }

  /**
   * Add projectile.
   *
   * @param c the c
   */
  public void addProjectile(Container c) {
    this.projectiles.put(c.getId(), c);
  }

  public void removeProjectile(Container c) {
    this.projectiles.remove(c.getId());
  }

  public void addNewShot(Container c) {
    this.newShots.put(c.getId(), c);
  }

  public void removeNewShot(Container c) {
    this.newShots.remove(c.getId());
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

  public ConcurrentHashMap<String, Container> getProjectiles() {
    return projectiles;
  }

  public int playerSize() {
    return players.size();
  }

  public Collection<String> getPlayerNames() {
    return players.keySet();
  }

}
