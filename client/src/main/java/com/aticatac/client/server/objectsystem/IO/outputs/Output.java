package com.aticatac.client.server.objectsystem.IO.outputs;

/**
 * The type Output.
 */
public abstract class Output {
  /**
   * Finalise output.
   *
   * @return the output
   */
  public abstract Output finalise();

  /**
   * Reset.
   */
  public abstract void reset();
}
