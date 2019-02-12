package com.aticatac.client.objectsystem;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class OrthoCamera extends OrthographicCamera {

    private Component c;


    public OrthoCamera (float viewportWidth, float viewportHeight,Component component) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.near = 0;
        c = component;

        update();
    }


    @Override
    public void update () {
        Position p = c.gameObject.getComponent(Transform.class).GetPosition();
        super.position.set((float)p.x,(float)p.y,10);
        super.update();
    }
}
