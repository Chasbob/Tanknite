package com.aticatac.common.model.Updates;

import com.aticatac.common.model.Model;
import com.aticatac.common.objectsystem.Container2;
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
  LinkedHashMap<String, Container2> players;
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

  public Container2 getI(int i) {
    return this.players.get(this.players.keySet().toArray()[i]);
  }

  public Container2 getMe(String id) {
    return players.getOrDefault(id, null);
  }

  /**
   * Gets players.
   *
   * @return the players
   */
  public HashMap<String, Container2> getPlayers() {
    return players;
  }

  public void addPlayer(Container2 c) {
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
