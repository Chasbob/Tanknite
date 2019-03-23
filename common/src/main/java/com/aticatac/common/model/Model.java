package com.aticatac.common.model;

/**
 * The type Model.
 */
public abstract class Model {
  private final String className;
  public String id;

  /**
   * Instantiates a new Model.
   *
   * @param id the id
   */
  public Model(String id) {
    this.id = id;
    className = this.getClass().getName();
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public boolean isModelType(String model) {
    return this.getClassName().equals(model);
  }

  public String getClassName() {
    return className;
  }
}
