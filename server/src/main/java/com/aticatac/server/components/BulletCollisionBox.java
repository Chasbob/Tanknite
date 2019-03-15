package com.aticatac.server.components;

import com.aticatac.server.components.transform.Position;
import com.aticatac.server.objectsystem.GameObject;
import java.util.ArrayList;

public class BulletCollisionBox extends Component {

  /***/
  private ArrayList<Position> box = new ArrayList<>();

  /**
   *
   * @param gameObject
   */
  public BulletCollisionBox(GameObject gameObject) {
    super(gameObject);
  }

  /**
   *
   * @param bulletPosition
   */
  public void setCollisionBox(Position bulletPosition) {

    if(box!=null){
      box.clear();
    }

    int lowerY = bulletPosition.getY() - 5;
    int lowerX = bulletPosition.getX() - 3;

    int higherY = bulletPosition.getY() + 5;
    int higherX = bulletPosition.getX() + 3;

    //positions for bottom of box
    for (int x = 0; x < 7; x++) {

      Position position = new Position(lowerX+x, lowerY);
      box.add(position);
    }

    //positions for left of box
    for (int y = 0; y < 11; y++) {

      Position position = new Position(lowerX, lowerY+y);
      box.add(position);

    }

    //positions for top of box
    for (int x = 0; x < 7; x++) {

      Position position = new Position(lowerX + x, higherY);
      box.add(position);

    }

    //positions for right of box
    for (int y = 0; y < 11; y++) {

      Position position = new Position(higherX, lowerY + y);
      box.add(position);

    }

  }


  /**
   *
   * @return
   */
  public ArrayList<Position> getCollisionBox() {

    return box;

  }

}
