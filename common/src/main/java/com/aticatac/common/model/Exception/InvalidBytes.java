package com.aticatac.common.model.Exception;

/**
 * The type Invalid bytes.
 */
public class InvalidBytes extends Exception {
  /**
   * Instantiates a new Invalid bytes.
   */
  public InvalidBytes() {
  }

  /**
   * Instantiates a new Invalid bytes.
   *
   * @param message the message
   */
  public InvalidBytes(String message) {
    super(message);
  }

  /**
   * Instantiates a new Invalid bytes.
   *
   * @param message the message
   * @param cause   the cause
   */
  public InvalidBytes(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Instantiates a new Invalid bytes.
   *
   * @param cause the cause
   */
  public InvalidBytes(Throwable cause) {
    super(cause);
  }

  /**
   * Instantiates a new Invalid bytes.
   *
   * @param message            the message
   * @param cause              the cause
   * @param enableSuppression  the enable suppression
   * @param writableStackTrace the writable stack trace
   */
  public InvalidBytes(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
