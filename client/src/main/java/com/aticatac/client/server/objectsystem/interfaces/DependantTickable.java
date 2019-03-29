package com.aticatac.client.server.objectsystem.interfaces;

/**
 * The interface Dependant tickable.
 *
 * @param <I> the type parameter
 */
public interface DependantTickable<I> extends Tickable {
  /**
   * Add frame.
   *
   * @param frame the frame
   */
  void addFrame(I frame);

  /**
   * Add and consume.
   *
   * @param frame the frame
   */
  void addAndConsume(I frame);
}
