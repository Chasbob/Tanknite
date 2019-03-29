package com.aticatac.client.isometric;

import com.aticatac.common.objectsystem.containers.Container;

import java.util.ArrayList;

/**
 * The type Iso container.
 */
public class IsoContainer extends Container {
  /**
   * Instantiates a new Iso container.
   *
   * @param container the container
   */
  public IsoContainer(Container container) {
    super(container.getX(), container.getY(), container.getR(), container.getId(), container.getObjectType());
  }

  /**
   * Gets degrees.
   *
   * @return the degrees
   */
  public ArrayList<Integer> getDegrees() {
    return degrees;
  }

  /**
   * Gets power.
   *
   * @return the power
   */
  public ArrayList<Integer> getPower() {
    return power;
  }

  private ArrayList<Integer> degrees = new ArrayList<>();
  private ArrayList<Integer> power = new ArrayList<>();

  /**
   * Add light.
   *
   * @param degree the degree
   * @param power  the power
   */
  public void addLight(int degree, int power) {
    this.degrees.add(degree);
    this.power.add(power);
  }

  /**
   * Gets light.
   *
   * @return the light
   */
  public String getLight() {
    if (degrees.size() >= 1) return "Degree: " + degrees.get(0);
    else return "none";
  }


}
