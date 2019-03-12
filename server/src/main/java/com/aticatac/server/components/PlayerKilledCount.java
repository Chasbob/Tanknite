package com.aticatac.server.components;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;

public class PlayerKilledCount extends Component {

  /***/
  private int playerKilledCount = 0;

  /**
   *
   * @param gameObject
   */
  public PlayerKilledCount(GameObject gameObject) {
    super(gameObject);
  }

  /**
   *
   * @param count
   */
  public void setPlayerKilledCount(int count){

    playerKilledCount = count;

  }

  /**
   *
   * @return
   */
  public int getPlayerKilledCount(){
    return playerKilledCount;
  }

}
