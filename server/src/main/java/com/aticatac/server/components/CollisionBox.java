package com.aticatac.server.components;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.objectsystem.GameObject;

import java.util.ArrayList;

public class CollisionBox extends Component {

  /***/
  private ArrayList<Position> box = new ArrayList<>();

  /**
   *
   * @param gameObject
   */
  public CollisionBox(GameObject gameObject) {
    super(gameObject);
  }

  /**
   *
   * @param tankPosition
   */
  public void setCollisionBox(Position tankPosition) {

    if(box!=null){
      box.clear();
    }

        int lowerY = tankPosition.getY() - 14;
        int lowerX = tankPosition.getX() - 14;

        int higherY = tankPosition.getY() + 14;
        int higherX = tankPosition.getX() + 14;

        //positions for bottom of box
        for (int x = 0; x < 29; x++) {

            Position position = new Position(lowerX+x, lowerY);
            box.add(position);
        }

        //positions for left of box
        for (int y = 0; y < 29; y++) {

            Position position = new Position(lowerX, lowerY+y);
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
   *
   * @return
   */
  public ArrayList<Position> getCollisionBox() {

    return box;

  }


  /**
   *
   * @param box
   * @param tankID
   */
  public void addBoxToData(ArrayList<Position> box, String tankID){

    for(int i=0; i<box.size(); i++){

      Position position = box.get(i);
      DataServer.INSTANCE.setCoordinates(position, tankID);

    }

  }

  /**
   *
   * @param box
   */
  public void removeBoxFromData(ArrayList<Position> box){

    for(int i=0; i<box.size(); i++){

      Position position = box.get(i);
      DataServer.INSTANCE.deleteCoordinates(position);

    }

  }




}
