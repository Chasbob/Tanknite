package com.aticatac.server.components;

import com.aticatac.server.components.transform.Position;
import com.aticatac.server.objectsystem.DataServer;
import com.aticatac.server.objectsystem.GameObject;
import java.util.ArrayList;

/**
 * The type Collision box.
 */
public class CollisionBox extends Component {
  /***/
  private ArrayList<Position> box = new ArrayList<>();

  /**
   * Instantiates a new Collision box.
   *
   * @param gameObject the game object
   */
  public CollisionBox(GameObject gameObject) {
    super(gameObject);
  }

  /**
   * Gets collision box.
   *
   * @return collision box
   */
  public ArrayList<Position> getCollisionBox() {
    return box;
  }

  /**
   * Sets collision box.
   *
   * @param tankPosition the tank position
   */
  public void setCollisionBox(Position tankPosition) {
    if (box != null) {
      box.clear();
    }
    int lowerY = tankPosition.getY() - 14;
    int lowerX = tankPosition.getX() - 14;
    int higherY = tankPosition.getY() + 14;
    int higherX = tankPosition.getX() + 14;
    //positions for bottom of box
    for (int x = 0; x < 29; x++) {
      Position position = new Position(lowerX + x, lowerY);
      box.add(position);
    }
    //positions for left of box
    for (int y = 0; y < 29; y++) {
      Position position = new Position(lowerX, lowerY + y);
      box.add(position);
    }
    //positions for top of box
    for (int x = 0; x < 29; x++) {
      Position position = new Position(lowerX + x, higherY);
      box.add(position);
    }
    //positions for right of box
    for (int y = 0; y < 29; y++) {
      Position position = new Position(higherX, lowerY + y);
      box.add(position);
    }
  }

  /**
   * Add box to data.
   *
   * @param box    the box
   * @param tankID the tank id
   */
  public void addBoxToData(ArrayList<Position> box, String tankID) {
    for (int i = 0; i < box.size(); i++) {
      Position position = box.get(i);
      DataServer.INSTANCE.setCoordinates(position, getGameObject().getEntity());
    }
  }

  /**
   * Remove box from data.
   *
   * @param box the box
   */
  public void removeBoxFromData(ArrayList<Position> box) {
    for (int i = 0; i < box.size(); i++) {
      Position position = box.get(i);
      DataServer.INSTANCE.deleteCoordinates(position);
    }
  }
}
