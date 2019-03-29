package com.aticatac.common.model.Updates;

import com.aticatac.common.model.Model;
import com.aticatac.common.objectsystem.containers.Container;
import com.aticatac.common.objectsystem.containers.KillLogEvent;
import com.aticatac.common.objectsystem.containers.PlayerContainer;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * The type Update.
 */
public class Update extends Model {
  /**
   * The Players.
   */
  private final ConcurrentHashMap<String, PlayerContainer> players;
  private final ConcurrentHashMap<String, Container> projectiles;
  private final ConcurrentHashMap<Integer, Container> powerups;
  private final ConcurrentHashMap<String, Container> newShots;
  private final CopyOnWriteArraySet<KillLogEvent> killLogEvents;
  private final CopyOnWriteArraySet<KillLogEvent> powerupEvent;
  private boolean start;
  private boolean playersChanged;

  /**
   * Instantiates a new Update.
   */
  public Update() {
    super("update");
    this.players = new ConcurrentHashMap<>();
    this.playersChanged = true;
    this.start = false;
    this.projectiles = new ConcurrentHashMap<>();
    powerups = new ConcurrentHashMap<>();
    newShots = new ConcurrentHashMap<>();
    killLogEvents = new CopyOnWriteArraySet<>();
    powerupEvent = new CopyOnWriteArraySet<>();
  }

  /**
   * Gets powerup event.
   *
   * @return the powerup event
   */
  public CopyOnWriteArraySet<KillLogEvent> getPowerupEvent() {
    return powerupEvent;
  }

  /**
   * Gets kill log events.
   *
   * @return the kill log events
   */
  public CopyOnWriteArraySet<KillLogEvent> getKillLogEvents() {
    return killLogEvents;
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
   * Clear powerups.
   */
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
  public PlayerContainer getMe(String id) {
    return players.getOrDefault(id, null);
  }

  /**
   * Gets powerups.
   *
   * @return the powerups
   */
  public ConcurrentHashMap<Integer, Container> getPowerups() {
    return powerups;
  }

  /**
   * Gets players.
   *
   * @return the players
   */
  public ConcurrentHashMap<String, PlayerContainer> getPlayers() {
    return players;
  }

  /**
   * Add player.
   *
   * @param c the c
   */
  public void addPlayer(PlayerContainer c) {
    this.players.put(c.getId(), c);
  }

  /**
   * Remove player.
   *
   * @param c the c
   */
  public void removePlayer(Container c) {
    this.players.remove(c.getId());
  }

  /**
   * Remove powerup.
   *
   * @param c the c
   */
  public void removePowerup(Container c) {
    this.powerups.remove(c.hashCode());
  }

  /**
   * Add powerup.
   *
   * @param c the c
   */
  public void addPowerup(Container c) {
    this.powerups.put(c.hashCode(), c);
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

  /**
   * Add new shot.
   *
   * @param c the c
   */
  public void addNewShot(Container c) {
    this.newShots.put(c.getId(), c);
  }

  /**
   * Remove new shot.
   *
   * @param c the c
   */
  public void removeNewShot(Container c) {
    this.newShots.remove(c.getId());
  }

  /**
   * Remove projectile.
   *
   * @param c the c
   */
  public void removeProjectile(Container c) {
    this.projectiles.remove(c.getId());
  }

  /**
   * Is players changed boolean.
   *
   * @return the boolean
   */
  public boolean isPlayersChanged() {
    return playersChanged;
  }

  @Override
  public String toString() {
    String toString = "Update{" +
        ", players=" + players.toString() +
        ", playersChanged=" + playersChanged +
        '}';
    return toString;
  }

  /**
   * Gets projectiles.
   *
   * @return the projectiles
   */
  public ConcurrentHashMap<String, Container> getProjectiles() {
    return projectiles;
  }

  /**
   * Projectile map concurrent hash map.
   *
   * @return the concurrent hash map
   */
  public ConcurrentHashMap<String, Container> projectileMap() {
    return projectiles;
  }

  /**
   * Player size int.
   *
   * @return the int
   */
  public int playerSize() {
    return players.size();
  }

  /**
   * Gets player names.
   *
   * @return the player names
   */
  public Collection<String> getPlayerNames() {
    return players.keySet();
  }

  /**
   * Gets new shots.
   *
   * @return the new shots
   */
  public ConcurrentHashMap<String, Container> getNewShots() {
    return newShots;
  }
}
