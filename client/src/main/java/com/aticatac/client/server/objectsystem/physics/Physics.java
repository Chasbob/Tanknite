package com.aticatac.client.server.objectsystem.physics;

import com.aticatac.client.server.Position;
import com.aticatac.client.server.game.BaseGame;
import com.aticatac.client.server.objectsystem.DataServer;
import com.aticatac.client.server.objectsystem.Entity;
import com.aticatac.common.model.Vector;
import com.aticatac.common.model.VectorF;
import com.aticatac.common.objectsystem.EntityType;
import java.util.HashSet;

/**
 * The type Physics.
 */
@SuppressWarnings("ALL")
public class Physics {
  /*** The gravity acting for all objects*/
  private static int gravity = 1;
  /*** The D.*/
  private final DataServer d;
  /**Entity that the physics is being added to*/
  private final Entity entity;
  /*** The mass of this object*/
  private int objectMass = 1;
  /*** The thrust for this tank*/
  private int thrust = 1;
  /*** The acceleration for this tank*/
  private int acceleration;
  /*** The velocity for this tank*/
  private int velocity;
  /**Position of the entity*/
  private Position position;
  /**rotation of the entity*/
  private int rotation;

  /**
   * Constructor.
   *
   * @param type the entity type that the physics object is created in
   * @param name the name of the entity that the physics object is created in
   */
  public Physics(final EntityType type, final String name) {
    this.position = Position.zero.copy();
    this.entity = new Entity(name, type);
    d = DataServer.INSTANCE;
  }

  /**
   * Move method to move the entity.
   *
   * @param rotation     The rotation of the entity
   * @param position     The position of the entity when move was called
   * @param speedPowerUp An integer representing whether a speedpowerup is currently active for the entity
   * @return A physics Response containing the new position and any collisions that happened when attempting to move
   */
  public PhysicsResponse move(int rotation, final Position position, int speedPowerUp) {

    //The position and coordinates of the oldposition
    this.position = position;
    int xCoord = this.position.getX();
    int yCoord = this.position.getY();
    //the change in time
    int dt = 1;
    //set the acceleration and velocity
    setAcceleration("positive", speedPowerUp);
    velocity += acceleration*dt;

    //calculate how far the entity will move
    int distance = dt * velocity;

    //calculate the new position using the distance and vectors
    double xr = Math.cos(Math.toRadians(rotation));
    double distanceX = distance * -xr;
    double yr = Math.sin(Math.toRadians(rotation));
    double distanceY = distance * -yr;
    double newX = xCoord + distanceX;
    double newY = yCoord + distanceY;
    //new position that it will attempt to move to
    Position newPosition = new Position((int) newX, (int) newY);

    //calculations for the positions between the old and new
    double dx = (newX - xCoord);
    double dy = (newY - yCoord);
    double ddx = Math.ceil(dx / entity.getType().radius);
    double ddy = Math.ceil(dy / entity.getType().radius);
    double x = 0;
    double y = 0;
    //creates a vector for the old position
    VectorF oV = new VectorF(position.getX(), position.getY());
    //creates a vector for the new position
    VectorF v = new VectorF((float) newX, (float) newY);
    //creates a scalar vector
    VectorF scl = v.cpy().sub(oV);
    //creates a set of collisions for the positions between the old and new
    HashSet<Entity> collisions = new HashSet<>();

    //checks all the positions between the old and new coordinates for collisions
    for (int i = 0; i < 10; i++) {
      oV = v.cpy().add(scl.cpy().scl(i * 0.1f));
      final Position newPosition1 = new Position((int) oV.x, (int) oV.y);
      //adds any collisions checking them using collisions2()
      collisions.addAll(collision2(newPosition1));
    }

    return new PhysicsResponse(collisions, newPosition);
  }


  /**
   * Checks a given position and its collision box against a set of occupied positions to see if the given position collides.
   * @param newPosition the position to be tested for collisions
   * @return A set of the entities a  position has collided with based on its collision box
   */
  private HashSet<Entity> collision2(Position newPosition) {

    //set of entities that have been collided with
    HashSet<Entity> collisions = new HashSet<>();
    //collision box for the position being tested
    CollisionBox box = new CollisionBox(newPosition, entity.getType());

    //checks each position of the box against a map of occupied positions
    for (Position p : box.getBox()) {
      Entity collision = findCollision(p);

      //adds the entity to the collisions set if it collides
      if (collision.getType() != EntityType.NONE) {
        collisions.add(collision);
      }
    }
    return collisions;
  }

  /**
   * Finds collisions by checking a given position against the basegame map
   * then getting the entity type if it is in the map.
   * @param p the position to be tested for collisions
   * @return the Entity that has been collided with
   */
  private Entity findCollision(Position p) {

    //if the position is in the map
    if (BaseGame.inMap(new Vector(p.getX(), p.getY()))) {
      //returns the entity type it is
      return d.INSTANCE.getEntity(p);
    }
    //else returns the outofbounds entity
    else {
      return Entity.outOfBounds;
    }
  }


  /**
   * Sets the acceleration for the entity
   * @param sign String representing "positive" for positive acceleration or "negative" for deceleration
   * @param speedPowerUp integer that represents if a speedpowerup is currently active, >0 if it is active.
   */
  private void setAcceleration(String sign, int speedPowerUp) {

    //acceleration
    if(sign.equals("positive")) {

      //if there is an active speedpowerup velocity can go above maximum
      if(speedPowerUp>0){
        if(velocity < (entity.getType().velocity)*2) {
          acceleration = (gravity * ((0 + objectMass) + thrust)) / objectMass;
        }else{
          acceleration = 0;
        }
      }

      //else the velocity can only reach a maximum
      else {

        //velocity is less than the maximum velocity for the entity type
        if (velocity < entity.getType().velocity) {
          acceleration = (gravity * ((0 + objectMass) + thrust)) / objectMass;
        }

        //the velocity is more than or equal to the enetity type velocity
        else if (velocity >= entity.getType().velocity) {

          //so set the velcoity to the maximum
          //and don't allow it to increase (set acceleration to 0)
          velocity = entity.getType().velocity;
          acceleration = 0;
        }

      }
    }

    //decceleration
    else{
      acceleration = -(gravity *((0 + objectMass) + thrust))/objectMass;
    }

  }


}
