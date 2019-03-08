package com.aticatac.common.model.Updates;

import com.aticatac.common.model.Model;
import com.aticatac.common.objectsystem.Container;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * The type Update.
 */
public class Update extends Model {
  private final boolean changed;
  /**
   * The Players.
   */
  LinkedHashMap<String, Container> players;
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
  }

  public Container getI(int i) {
    return this.players.get(this.players.keySet().toArray()[i]);
  }

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

  public void addPlayer(Container c) {
    this.players.put(c.getId(), c);
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
}
