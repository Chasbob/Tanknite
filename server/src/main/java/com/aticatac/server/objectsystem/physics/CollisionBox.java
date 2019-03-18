package com.aticatac.server.objectsystem.physics;

import com.aticatac.server.components.transform.Position;
import com.aticatac.server.objectsystem.Entity;
import java.util.ArrayList;
import java.util.Objects;
import org.apache.log4j.Logger;

public class CollisionBox {
  private final int radius;
  private final Logger logger;
  private Position position;
  private ArrayList<Position> box;

  public CollisionBox(final Position position, final int radius) {
    this.position = position;
    this.radius = radius;
    this.box = calcCollisionBox(this.position);
    this.logger = Logger.getLogger(getClass());
  }

  public CollisionBox(final Position position, final Entity.EntityType type) {
    this(position, type.radius);
  }

  public void setPosition(Position p) {
    this.position = p;
    box = calcCollisionBox(position);
  }

  private ArrayList<Position> calcCollisionBox(final Position p) {
    ArrayList<Position> out = new ArrayList<>();
    int lowerY = p.getY() - radius;
    int lowerX = p.getX() - radius;
    int higherY = p.getY() + radius;
    int higherX = p.getX() + radius;
    //positions for bottom of out
    for (int x = 0; x < radius * 2; x++) {
      Position position = new Position(lowerX + x, lowerY);
      out.add(position);
    }
    //positions for left of out
    for (int y = 0; y < radius * 2; y++) {
      Position position = new Position(lowerX, lowerY + y);
      out.add(position);
    }
    //positions for top of out
    for (int x = 0; x < radius * 2; x++) {
      Position position = new Position(lowerX + x, higherY);
      out.add(position);
    }
    //positions for right of out
    for (int y = 0; y < radius * 2; y++) {
      Position position = new Position(higherX, lowerY + y);
      out.add(position);
    }
    return out;
  }

  public ArrayList<Position> getBox() {
    return box;
  }

  @Override
  public int hashCode() {
    return Objects.hash(radius, position, getBox());
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final CollisionBox that = (CollisionBox) o;
    return radius == that.radius &&
        position.equals(that.position) &&
        getBox().equals(that.getBox());
  }
}
