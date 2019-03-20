//package com.aticatac.server.components.physics;
//
//import java.util.Objects;
//
//public class Entity {
//  public static final Entity empty = new Entity("", EntityType.NONE);
//  public final String name;
//  public final EntityType type;
//
//  public Entity(final String name, final EntityType type) {
//    this.name = name;
//    this.type = type;
//  }
//
//  public Entity(final EntityType type) {
//    this.name = "";
//    this.type = type;
//  }
//
//  public Entity() {
//    this("", EntityType.NONE);
//  }
//
//  @Override
//  public int hashCode() {
//    return Objects.hash(getName(), getType());
//  }
//
//  @Override
//  public boolean equals(final Object o) {
//    if (this == o) {
//      return true;
//    }
//    if (o == null || getClass() != o.getClass()) {
//      return false;
//    }
//    final Entity that = (Entity) o;
//    return getName().equals(that.getName()) &&
//        getType() == that.getType();
//  }
//
//  @Override
//  public String toString() {
//    return "Entity{" +
//        "name='" + name + '\'' +
//        ", type=" + type +
//        '}';
//  }
//
//  public String getName() {
//    return name;
//  }
//
//  public EntityType getType() {
//    return type;
//  }
//
//  public enum EntityType {
//    NONE, TANK(14), BULLET(2), WALL(15), AMMO_POWERUP, SPEED_POWERUP, HEALTH_POWERUP, BULLET_POWERUP;
//    public final int radius;
//
//    EntityType(final int radius) {
//      this.radius = radius;
//    }
//
//    EntityType() {
//      this.radius = 0;
//    }
//
//    public boolean isPowerUp() {
//      return this.toString().contains("POWERUP");
//    }
//  }
//}
//
