package com.aticatac.common.components.transform;

//import com.aticatac.server.components.DataServer;

/**
 * The type Position.
 */
public class Position {
    /**
     * The x and y coordinates for the position
     */
    private double x, y;

    /**
     * construct for a position where the values are not yet set
     */
    public Position() {
        this(0, 0);
    }

    /**
     * construct for a position where the values are known
     *
     * @param xs the X value
     * @param ys the Y value
     */
    public Position(double xs, double ys) {
        x = xs;
        y = ys;
    }

    /**
     * Gets the x value for the position
     *
     * @return the X value
     */
    public double getX() {
        return x;
    }

    /**
     * Sets x.
     *
     * @param x the x
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets the Y value for the position
     *
     * @return the Y value
     */
    public double getY() {
        return y;
    }

    /**
     * Sets y.
     *
     * @param y the y
     */
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }


    //paramaters for the box
 /*   public void setCollisionBox(Position tankPosition) {

        double lowerY = tankPosition.getY() - 16;
        double lowerX = tankPosition.getX() - 16;

        double higherY = tankPosition.getY() + 16;
        double higherX = tankPosition.getX() + 16;

        for (int x = 0; x < 33; x++) {

            Position position = new Position(lowerX + x, lowerY);
            DataServer.INSTANCE.setCoordinates(position, "tank");

        }

        for (int y = 0; y < 33; y++) {

            Position position = new Position(lowerX, lowerY + y);
            DataServer.INSTANCE.setCoordinates(position, "tank");

        }

        for (int x = 0; x < 33; x++) {

            Position position = new Position(lowerX + x, higherY);
            DataServer.INSTANCE.setCoordinates(position, "tank");

        }

        for (int y = 0; y < 33; y++) {

            Position position = new Position(higherX, higherY + y);
            DataServer.INSTANCE.setCoordinates(position, "tank");

        }

    }*/

}
