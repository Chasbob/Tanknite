package com.aticatac.client.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.apache.log4j.Logger;

/**
 * The type Camera.
 */
public class Camera {
  private final float maxX;
  private final float maxY;
  private final float width;
  private final float height;
  private final Logger logger;
  private OrthographicCamera camera;
  private Viewport viewport;
  private boolean iso;

  /**
   * Instantiates a new Camera.
   *
   * @param maxX   the max x
   * @param maxY   the max y
   * @param width  the width
   * @param height the height
   * @param iso    the iso
   */
  public Camera(float maxX, float maxY, float width, float height, boolean iso) {
    this.maxX = maxX;
    this.maxY = maxY;
    this.width = width;
    this.height = height;
    this.camera = new OrthographicCamera();
    this.viewport = new ExtendViewport(width, height, camera);
    this.viewport.apply(true);
    logger = Logger.getLogger(getClass());
    this.camera.setToOrtho(false);
    this.iso = iso;
    if (iso) {
      this.camera.zoom = 0.5f;
    }
  }

  /**
   * Gets viewport.
   *
   * @return the viewport
   */
  public Viewport getViewport() {
    return viewport;
  }

  /**
   * Gets camera.
   *
   * @return the camera
   */
  public OrthographicCamera getCamera() {
    return camera;
  }

  /**
   * Gets x.
   *
   * @return the x
   */
  public float getX() {
    return this.camera.position.x;
  }

  /**
   * Gets y.
   *
   * @return the y
   */
  public float getY() {
    return this.camera.position.y;
  }

  /**
   * Update.
   */
  public void update() {
    this.camera.update();
  }

  /**
   * Sets posititon.
   *
   * @param x the x
   * @param y the y
   */
  public void setPosititon(float x, float y) {
    if (this.iso) {
      camera.position.x = x;
      camera.position.y = y;
    } else {
      camera.position.x = MathUtils.clamp(x, camera.viewportWidth / 2f, 1920f - camera.viewportWidth / 2f);
      camera.position.y = MathUtils.clamp(y, camera.viewportHeight / 2f, 1920f - camera.viewportHeight / 2f);
    }
    camera.update();
  }
}
