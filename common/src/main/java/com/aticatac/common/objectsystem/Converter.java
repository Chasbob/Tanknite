package com.aticatac.common.objectsystem;

import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.model.TransformModel;

import java.util.ArrayList;

public class Converter {

    public static ArrayList<Container> Deconstructor(GameObject g){
        var array = new ArrayList<Container>();

        var cont = new Container();
        cont.transformModel = new TransformModel();
        com.aticatac.common.components.transform.Transform transform = g.getComponent(com.aticatac.common.components.transform.Transform.class);
        cont.transformModel.x = transform.getPosition().x;
        cont.transformModel.y = transform.getPosition().y;
        cont.transformModel.r = transform.GetRotation();
        array.add(cont);

        for(var c: g.getChildren()){
            array.addAll(Deconstructor(c));
        }

        return array;
    }

    public static GameObject Constructor(ArrayList<Container> containerArrayList){
        try {
            var root = new GameObject("Root");

            for (var c:containerArrayList) {
                var obj = new GameObject("Obj",root);
                obj.transform.SetTransform(c.transformModel.x,c.transformModel.y);

                return root;
            }
        } catch (Exception e){}

        return null;
    }
}
