package com.aticatac.common.objectsystem;

import com.aticatac.common.components.Texture;
import com.aticatac.common.components.transform.Transform;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class Converter {
    private static Logger logger = Logger.getLogger(Converter.class);

    public static ArrayList<Container> deconstruct(GameObject g) {
        HashMap<String, Container> containers = new HashMap<>();
        var array = new ArrayList<Container>();
        Transform transform = g.getComponent(Transform.class);
        Container cont;
        if (g.componentExists(Texture.class)) {
            cont = new Container(transform.getX(), transform.getY(), transform.getRotation(), Optional.of(g.getComponent(Texture.class).Texture));
        } else {
            cont = new Container(transform.getX(), transform.getY(), transform.getRotation());
        }
        array.add(cont);
//        containers.put(g.getName(), cont);
        for (var c : g.getChildren()) {
            array.addAll(deconstruct(c));
        }
        return array;
    }

    public static GameObject construct(ArrayList<Container> containerArrayList) {
        try {
            var root = new GameObject("Root");
            for (var c : containerArrayList) {
                var obj = new GameObject("Obj", root);
                obj.setTransform(c.getX(), c.getY());
                if (c.getTexture().isPresent()) {
                    obj.addComponent(Texture.class).Texture = c.getTexture().get();
                }
            }
            return root;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }
}
