package com.aticatac.common.components;

public class delete {

  //TODO see if we need this part.
        /*//Below can be the only part of this that is checked and will then return the type that it is
        double changeY = newPosition.getY() - oldPosition.getY();
        double changeX = newPosition.getX() - oldPosition.getX();*/

        /*//checks everywhere from old to new position for collision
        for(int i=0; i<changeX+1; i++){
            for(int j=0; j<changeY+1; j++){

                Position position = new Position();
                //Calculates the new coordinates
                if (direction.equals("down")) {
                    //only moving on y coord
                    position = new Position(oldPosition.getX(), oldPosition.getY()+j);
                }
                if (direction.equals("up")) {
                    //only moving on y coord
                    position = new Position(oldPosition.getX(), oldPosition.getY()-j);
                }
                if (direction.equals("left")) {
                    //only moving on x coord
                    position = new Position(oldPosition.getX()+i, oldPosition.getY());
                }
                else if (direction.equals("right")) {
                    //only moving on x coord
                    position = new Position(oldPosition.getX() - i, oldPosition.getY());
                }

                Object[] returnPosition = getCollisionArray(newPosition, oldPosition, occupiedCoordinates, position);
                if (returnPosition != null) return returnPosition;

            }
        }*/


}
