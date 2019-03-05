package com.aticatac.client.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import org.apache.log4j.Logger;

public class Camera {
    private final float maxX;
    private final float maxY;
    private final float width;
    private final float height;
    private final Logger logger;
    private OrthographicCamera camera;
    private float tankx;
    private float tanky;

    public Camera(float maxX, float maxY, float width, float height) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.width = width;
        this.height = height;
        this.camera = new OrthographicCamera(width, height);
        logger = Logger.getLogger(getClass());
        this.camera.setToOrtho(false);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public float getX() {
        return this.camera.position.x;
    }

    public float getY() {
        return this.camera.position.y;
    }

    public void update() {
        this.camera.update();
    }

    public void setPosititon(float x, float y) {
//        float newX = maxX - Math.abs(x);
//        float newY = maxY - Math.abs(y);
        float newX = x;
        float newY = y;
        this.tankx = newX;
        this.tanky = newY;
//        if (newX < (width / 2)) {
//            newX = width / 2;
//        } else if (newX > maxX - (this.width / 2)) {
//            newX = maxX - (this.width / 2);
//        }
//        if (newY < (this.height / 2)) {
//            newY = (this.height / 2);
//        } else if (newY > maxY - (this.height / 2)) {
//            newY = maxY - (this.height / 2);
//        }
        camera.position.x = MathUtils.clamp(newX, camera.viewportWidth / 2f, 1920f - camera.viewportWidth / 2f);
        camera.position.y = MathUtils.clamp(newY, camera.viewportHeight / 2f, 1920f - camera.viewportHeight / 2f);
    }
//        this.camera.position.set(maxX - newX, maxY - newY, 0);
//    }
}
