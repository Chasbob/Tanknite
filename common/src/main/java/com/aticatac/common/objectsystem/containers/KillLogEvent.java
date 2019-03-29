package com.aticatac.common.objectsystem.containers;

/**
 * The type Kill log event.
 */
public class KillLogEvent {
  private final String killer;
  private final String killed;

  /**
   * Instantiates a new Kill log event.
   *
   * @param killer the killer
   * @param killed the killed
   */
  public KillLogEvent(final String killer, final String killed) {
    this.killer = killer;
    this.killed = killed;
  }

  /**
   * Gets killer.
   *
   * @return the killer
   */
  public String getKiller() {
    return killer;
  }

  /**
   * Gets killed.
   *
   * @return the killed
   */
  public String getKilled() {
    return killed;
  }
}
