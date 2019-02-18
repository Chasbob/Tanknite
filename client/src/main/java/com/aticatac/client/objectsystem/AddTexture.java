package com.aticatac.client.objectsystem;

import com.aticatac.common.objectsystem.GameObject;


public class AddTexture {

    public static void addBulletTexture(GameObject g){
        try {
            g.addComponent(Renderer.class);
            g.getComponent(Renderer.class).setTexture("img/bullet.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
