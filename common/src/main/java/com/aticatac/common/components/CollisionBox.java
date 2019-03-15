package com.aticatac.common.components;

import com.aticatac.common.components.transform.Position;
import com.aticatac.common.objectsystem.GameObject;

import java.util.ArrayList;

public class CollisionBox extends Component {

  /***/
  private ArrayList<Position> box = new ArrayList<>();

  public CollisionBox(GameObject gameObject) {
    super(gameObject);
  }

  /**
   *
   * @param tankPosition
   */
  public void setCollisionBox(Position tankPosition) {

    if(box != null) {
      box.clear();
    }

        int lowerY = tankPosition.getY() - 30;
        int lowerX = tankPosition.getX() - 30;

        int higherY = tankPosition.getY()-2;
        int higherX = tankPosition.getX()-2;

        //positions for bottom of box
        for (int x = 0; x < 31; x++) {

            Position position = new Position(lowerX+x, lowerY);
            box.add(position);
        }

        //positions for left of box
        for (int y = 0; y < 31; y++) {

            Position position = new Position(lowerX, lowerY+y);
            box.add(position);

        }

        //positions for top of box
        for (int x = 0; x < 31; x++) {

            Position position = new Position(lowerX + x, higherY);
            box.add(position);

        }

        //positions for right of box
        for (int y = 0; y < 31; y++) {

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


  public void addBoxToData(ArrayList<Position> box, String tankID){

    for(int i=0; i<box.size(); i++){

      Position position = box.get(i);
      DataServer.INSTANCE.setCoordinates(position, tankID);

    }

  }

  public void removeBoxFromData(ArrayList<Position> box){

    for(int i=0; i<box.size(); i++){

      Position position = box.get(i);
      DataServer.INSTANCE.deleteCoordinates(position);

    }

  }




}
