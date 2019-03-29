package com.aticatac.client.server.bus.event;

import com.aticatac.common.objectsystem.containers.Container;

/**
 * The type Powerups changed event.
 */
public class PowerupsChangedEvent {
  private final Action action;
  private final Container container;
  private final Container consumer;

  /**
   * Instantiates a new Powerups changed event.
   *
   * @param action    the action
   * @param container the container
   */
  public PowerupsChangedEvent(final Action action, final Container container) {
    this.action = action;
    this.container = container;
    consumer = new Container();
  }

  /**
   * Instantiates a new Powerups changed event.
   *
   * @param action    the action
   * @param container the container
   * @param consumer  the consumer
   */
  public PowerupsChangedEvent(final Action action, final Container container, final Container consumer) {
    this.action = action;
    this.container = container;
    this.consumer = consumer;
  }

  @Override
  public String toString() {
    return "PowerupsChangedEvent{" +
        "action=" + action +
        ", container=" + container +
        '}';
  }

  /**
   * Gets action.
   *
   * @return the action
   */
  public Action getAction() {
    return action;
  }

  /**
   * Gets consumer.
   *
   * @return the consumer
   */
  public Container getConsumer() {
    return consumer;
  }

  /**
   * Gets container.
   *
   * @return the container
   */
  public Container getContainer() {
    return container;
  }

  /**
   * The enum Action.
   */
  public enum Action {
    /**
     * Add action.
     */
    ADD,
    /**
     * Remove action.
     */
    REMOVE,
    /**
     * Update action.
     */
    UPDATE
  }
}
