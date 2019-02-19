package com.aticatac.client.util;

import com.aticatac.client.objectsystem.ObjectHelper;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.common.prefab.Tank;

public class GameManager {
    public GameObject root;

    public GameManager(){
        try {
            root = new GameObject("Root");

            //self = CreateTank("selfTank",0,0);
        }catch (Exception ignored){}
    }

    public void SetRotateTankTop(double d){
        //self.children.get(1).transform.SetRotation(Math.toDegrees(d)-90f);
    }

    public GameObject CreateTank(String name, double posX,double posY) {
        try {
            var tank = new Tank(name, root, new Position(posX, posY));
            ObjectHelper.AddRenderer(tank.getChildren().get(0), "img/TankBottom.png");
            ObjectHelper.AddRenderer(tank.getChildren().get(1), "img/TankTop.png");
            return tank;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
