package com.aticatac.client.objectsystem;

import com.aticatac.common.components.Texture;
import com.aticatac.common.objectsystem.GameObject;


public class AddTexture {

    public static void addTexture(GameObject g){
        try {
            Texture t = g.getComponent(Texture.class);
            g.addComponent(Renderer.class);
            g.getComponent(Renderer.class).setTexture(t.Texture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
