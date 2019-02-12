package com.aticatac.common.components.transform;

public class Position {

    /**The x and y coordinates for the position*/
    public double x,y;


    /**
     * Constructor for a position where the values are not yet set
     */
    public Position(){x = 0;y=0;}


    /**
     * Constructor for a position where the values are known
     * @param xs the X value
     * @param ys the Y value
     */
    public Position(double xs,double ys){x = xs;y=ys;}


    /**
     * Gets the x value for the position
     * @return the X value
     */
    public double getX(){
        return x;
    }


    /**
     * Gets the Y value for the position
     * @return the Y value
     */
    public double getY(){
        return y;
    }
}
