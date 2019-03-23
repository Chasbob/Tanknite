package com.aticatac.server.objectsystem.entities;

import com.aticatac.common.model.Command;
import com.aticatac.common.model.CommandModel;
import com.aticatac.common.model.Vector;
import com.aticatac.common.objectsystem.Container;
import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.ai.PlayerState;
import com.aticatac.server.bus.EventBusFactory;
import com.aticatac.server.bus.event.BulletsChangedEvent;
import com.aticatac.server.bus.event.PlayersChangedEvent;
import com.aticatac.server.bus.event.ShootEvent;
import com.aticatac.server.bus.event.TankCollisionEvent;
import com.aticatac.server.networking.Server;
import com.aticatac.server.objectsystem.DataServer;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.interfaces.Collidable;
import com.aticatac.server.objectsystem.interfaces.DependantTickable;
import com.aticatac.server.objectsystem.interfaces.Hurtable;
import com.aticatac.server.objectsystem.physics.Physics;
import com.aticatac.server.objectsystem.physics.PhysicsResponse;
import com.aticatac.server.transform.Position;
import com.google.common.eventbus.EventBus;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Tank extends Entity implements DependantTickable<CommandModel>, Hurtable {
  private final ConcurrentLinkedQueue<CommandModel> frames;
  private final Physics physics;
  private CommandModel input;
  private int health;
  private int maxHealth;
  private int maxAmmo;
  private int ammo;
  private boolean damageIncrease = false;
  private boolean speedIncrease = false;
  private boolean shuttingDown = false;
  private int framesToShoot;

  //todo add in a parameter boolean which is ai true or false
  //TODO add in the parameter changes everywhere
  public Tank(String name, Position p, int health, int ammo) {
    super(name, EntityType.TANK, p);
    frames = new ConcurrentLinkedQueue<>();
    setInput(new CommandModel("", Command.DEFAULT));
    this.setMaxHealth(100);
    this.setHealth(health);
    this.setAmmo(ammo);
    this.setMaxAmmo(30);
    physics = new Physics(getPosition(), getType(), name);
    setFramesToShoot(120);
  }

  @Override
  public void tick() {
    logger.trace("tick");
    if (getFrames().size() > 5) {
      getFrames().clear();
    }
    if (!getFrames().isEmpty()) {
      setInput(getFrames().poll());
      if (Math.abs(getRotation() - getInput().getBearing()) != 1) {
        this.logger.info(getRotation());
      }
      setRotation(getInput().getBearing());
      try {
        if (getHealth() > 10) {
          move(getInput().getVector(), isSpeedIncrease());
        }
      } catch (Exception e) {
        this.logger.error(e);
        this.logger.error("Error while moving.");
      }
    }
    if (getInput().getCommand() == Command.SHOOT) {
      this.logger.info("shoot");
      if (!(getAmmo() == 0 || getHealth() == 0) && getFramesToShoot() < 0) {
        setAmmo(getAmmo() - 1);
        if (isDamageIncrease()) {
          addBullet(new Bullet(this, getPosition().copy(), getInput().getBearing(), 20));
        } else {
          addBullet(new Bullet(this, getPosition().copy(), getInput().getBearing(), 10));
        }
        setFramesToShoot(60);
      }
    }
    if (getFramesToShoot() == 1) {
      this.logger.info("Ready to fire!");
    }
    setFramesToShoot(getFramesToShoot() - 1);
  }

  private void checkCollisions(PhysicsResponse response) {
    if (!response.getCollisions().contains(outOfBounds)) {
      for (Entity e :
          response.getCollisions()) {
        if (e.getType() != EntityType.NONE && e.getType() != EntityType.WALL && !e.getName().equals(getName())) {
          this.logger.info(e);
        }
        switch (e.getType()) {
          case NONE:
            break;
          case TANK:
            if (!e.getName().equals(getName())) {
              return;
            }
            break;
          case BULLET:
            break;
          case WALL:
            return;
          case OUTOFBOUNDS:
            return;
          case AMMO_POWERUP:
            int newAmmo = getAmmo() + 10;
            if (newAmmo > getMaxAmmo()) {
              setAmmo(getMaxAmmo());
            } else {
              setAmmo(newAmmo);
            }
            onPlayerHit(e);
            EventBusFactory.getEventBus().post(new TankCollisionEvent(this, e));
            break;
          case SPEED_POWERUP:
            // TODO: Implement thread for 20 seconds (in terms of ticks) where speedIncrease = true
            onPlayerHit(e);
            EventBusFactory.getEventBus().post(new TankCollisionEvent(this, e));
            break;
          case HEALTH_POWERUP:
            heal(10);
            onPlayerHit(e);
            EventBusFactory.getEventBus().post(new TankCollisionEvent(this, e));
            break;
          case DAMAGE_POWERUP:
            onPlayerHit(e);
            EventBusFactory.getEventBus().post(new TankCollisionEvent(this, e));
            break;
        }
      }
      setPosition(response.getPosition(), true);
    }
  }

  void move(Vector vector, final boolean speedIncrease) {
    if (!vector.equals(Vector.Zero)) {
      var response = getPhysics().move(vector.angle(), getPosition(), speedIncrease);
      checkCollisions(response);
    }
    EventBusFactory.getEventBus().post(new PlayersChangedEvent(PlayersChangedEvent.Action.UPDATE, getContainer()));
  }

  public int getAmmo() {
    return ammo;
  }

  public void setAmmo(int ammo) {
    this.ammo = ammo;
  }

  @Override
  public int getHealth() {
    return health;
  }

  @Override
  public int hit(final int damage) {
    this.logger.info(clamp(getHealth() - damage));
    if (getHealth() <= 10 && getHealth() > 0) {
      // TODO, Thread to call remove player after 20 seconds (in terms of ticks)
    } else if (getHealth() <= 0) {
      Server.ServerData.INSTANCE.getGame().removePlayer(this.getName());
    }
    setHealth(clamp(getHealth() - damage));
    return getHealth();
  }

  @Override
  public int heal(final int amount) {
    setHealth(clamp(getHealth() + amount));
    return getHealth();
  }

  public void setHealth(int health) {
    this.health = health;
  }

  private int clamp(int value) {
    if (value < 0) {
      return 0;
    }
    if (value > getMaxHealth()) {
      return getMaxHealth();
    }
    return value;
  }

  public Container getContainer() {
    return new Container(this.getPosition().getX(),
        this.getPosition().getY(),
        this.getRotation(),
        this.getHealth(),
        this.getAmmo(),
        this.getName(),
        EntityType.TANK);
  }

  @Override
  public boolean intersects(final Collidable collidable) {
    ArrayList<Position> other = collidable.getCollisionBox().getBox();
    other.addAll(getCollisionBox().getBox());
    return other.size() != (new HashSet<>(other).size());
  }
//  public void move(int x, int y) {
//    setPosition(new Position(x, y), true);
//    EventBusFactory.getEventBus().post(new PlayersChangedEvent(PlayersChangedEvent.Action.UPDATE, getContainer()));
//  }

  @Override
  public void addToData() {
    DataServer.INSTANCE.addBoxToData(getCollisionBox(), this);
  }

  public PlayerState getPlayerState() {
    return new PlayerState(getPosition(), getHealth());
  }

  @Override
  public void addFrame(final CommandModel frame) {
    this.getFrames().add(frame);
  }

  @Override
  public void addAndConsume(final CommandModel frame) {
//    this.frames.add(frame);
//    this.tick();
  }

  /**
   * Add bullet.
   *
   * @param bullet the bullet
   */
  public void addBullet(Bullet bullet) {
    getBus().post(new ShootEvent(this, bullet));
    getBus().post(new BulletsChangedEvent(BulletsChangedEvent.Action.ADD, bullet.getContainer()));
//    output.addBullet(bullet);
  }

  public void onPlayerHit(Entity entity) {
//    getBus().post(new PlayersChangedEvent(PlayersChangedEvent.Action.UPDATE, getContainer()));
    getBus().post(new TankCollisionEvent(this, entity));
  }

  public ConcurrentLinkedQueue<CommandModel> getFrames() {
    return frames;
  }

  public Physics getPhysics() {
    return physics;
  }

  public EventBus getBus() {
    return EventBusFactory.getEventBus();
  }

  public CommandModel getInput() {
    return input;
  }

  public void setInput(CommandModel input) {
    this.input = input;
  }

  public int getMaxHealth() {
    return maxHealth;
  }

  public void setMaxHealth(int maxHealth) {
    this.maxHealth = maxHealth;
  }

  public int getMaxAmmo() {
    return maxAmmo;
  }

  public void setMaxAmmo(int maxAmmo) {
    this.maxAmmo = maxAmmo;
  }

  public boolean isDamageIncrease() {
    return damageIncrease;
  }

  public void setDamageIncrease(boolean damageIncrease) {
    this.damageIncrease = damageIncrease;
  }

  public boolean isSpeedIncrease() {
    return speedIncrease;
  }

  public void setSpeedIncrease(boolean speedIncrease) {
    this.speedIncrease = speedIncrease;
  }

  public boolean isShuttingDown() {
    return shuttingDown;
  }

  public void setShuttingDown(boolean shuttingDown) {
    this.shuttingDown = shuttingDown;
  }

  public int getFramesToShoot() {
    return framesToShoot;
  }

  public void setFramesToShoot(int framesToShoot) {
    this.framesToShoot = framesToShoot;
  }
}