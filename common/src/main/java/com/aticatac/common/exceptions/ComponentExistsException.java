package com.aticatac.common.exceptions;

public class ComponentExistsException extends Exception {
  public ComponentExistsException() {
  }

  public ComponentExistsException(String message) {
    super(message);
  }

  private ComponentExistsException(String message, Throwable cause) {
    super(message, cause);
  }

  private ComponentExistsException(Throwable cause) {
    super(cause);
  }

  private ComponentExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
