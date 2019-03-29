package com.aticatac.client.server.objectsystem.physics;

import com.aticatac.client.server.Position;
import com.aticatac.client.server.objectsystem.Entity;
import java.util.HashSet;

/**
 * The type Physics response.
 */
public class PhysicsResponse {

  /**A set of the entities another echecks all the positions between the old and new coordinates for collisionsntity has collided with*/
  private final HashSet<Entity> collisions;
  /**The new position that an entity is attempting to move to*/
  private final Position position;


  /**
   * Constructor
   *
   * @param collisions a set of the entities another entity collided with
   * @param position   the new position an entity is trying to move to
   */
  public PhysicsResponse(final HashSet<Entity> collisions, final Position position) {
    this.collisions = collisions;
    this.position = position;
  }

  /**
   * Gets the set of entities that have been collided with
   *
   * @return a set of the entities something has collided with
   */
  public HashSet<Entity> getCollisions() {
    return collisions;
  }

  /**
   * Gets the new position that is being moved to
   *
   * @return the new position that an entitiy is trying to move to
   */
  public Position getPosition() {
    return position;
  }

}
