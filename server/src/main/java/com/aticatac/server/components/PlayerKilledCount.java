package com.aticatac.server.components;

import com.aticatac.server.objectsystem.GameObject;

public class PlayerKilledCount extends Component {
  /***/
  private int playerKilledCount = 0;

  /**
   * @param gameObject
   */
  public PlayerKilledCount(GameObject gameObject) {
    super(gameObject);
  }

  /**
   * @return
   */
  public int getPlayerKilledCount() {
    return playerKilledCount;
  }

  /**
   * @param count
   */
  public void setPlayerKilledCount(int count) {
    playerKilledCount = count;
  }
}
