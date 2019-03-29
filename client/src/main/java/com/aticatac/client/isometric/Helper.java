package com.aticatac.client.isometric;


import com.aticatac.client.server.Position;

/**
 * The type Helper.
 */
public class Helper {
  //(60-y-1) * 32, (x+1) * 32

  /**
   * Tile to screen x int.
   *
   * @param x the x
   * @param y the y
   * @return the int
   */
  public static int tileToScreenX(int x, int y) {
    x = 1920 - x;
    y = 1920 - y;
    return (x + y) / 2;
  }

  /**
   * Tile to screen y int.
   *
   * @param x the x
   * @param y the y
   * @return the int
   */
  public static int tileToScreenY(int x, int y) {
    x = 1920 - x;
    y = 1920 - y;
    return (x - y) / 4;
  }

  /**
   * Tile to screen x float.
   *
   * @param x the x
   * @param y the y
   * @return the float
   */
  public static float tileToScreenX(float x, float y) {
    x = 1920 - x;
    y = 1920 - y;
    return (x + y) / 2;
  }

  /**
   * Tile to screen y float.
   *
   * @param x the x
   * @param y the y
   * @return the float
   */
  public static float tileToScreenY(float x, float y) {
    x = 1920 - x;
    y = 1920 - y;
    return (x - y) / 4;
  }

  /**
   * Tile to screen position.
   *
   * @param p the p
   * @return the position
   */
  public static Position tileToScreen(Position p) {
    int x = 1920 - p.getX();
    int y = 1920 - p.getY();
    return new Position((x + y) / 2, (x - y) / 4);
  }

  /**
   * Screen xto tile int.
   *
   * @param x the x
   * @param y the y
   * @return the int
   */
  public static int screenXtoTile(int x, int y) {
    return 1920 - ((2 * x) - y);
  }

  /**
   * Screen yto tile int.
   *
   * @param x the x
   * @param y the y
   * @return the int
   */
  public static int screenYtoTile(int x, int y) {
    return 1920 - (-(4 * y - x));
  }

  /**
   * Screen to tile position.
   *
   * @param p the p
   * @return the position
   */
  public static Position screenToTile(Position p) {
    var p2 = new Position();

    p2.setX((p.getX() * 2 + p.getY() * 4) / 2);
    p2.setY((p.getY() * 4 - (p.getX() * 2)) / 2);


    p2.setX(1920 - p2.getX());
    p2.setY(p2.getY() + 1920);
    return p2;
  }
}
