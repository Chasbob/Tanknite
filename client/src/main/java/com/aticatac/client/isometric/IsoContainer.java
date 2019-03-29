package com.aticatac.client.isometric;

import com.aticatac.common.objectsystem.containers.Container;

import java.util.ArrayList;

public class IsoContainer extends Container {
  public IsoContainer(Container container) {
    super(container.getX(), container.getY(), container.getR(), container.getId(), container.getObjectType());
  }

  public ArrayList<Integer> getDegrees() {
    return degrees;
  }

  public ArrayList<Integer> getPower() {
    return power;
  }

  private ArrayList<Integer> degrees = new ArrayList<>();
  private ArrayList<Integer> power = new ArrayList<>();

  public void addLight(int degree, int power) {
    this.degrees.add(degree);
    this.power.add(power);
  }

  public String getLight() {
    if (degrees.size() >= 1) return "Degree: " + degrees.get(0);
    else return "none";
  }


}
