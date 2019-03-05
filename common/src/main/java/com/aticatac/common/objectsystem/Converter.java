package com.aticatac.common.objectsystem;

import com.aticatac.common.components.Texture;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 * The type Converter.
 */
public class Converter {
    private static Logger logger = Logger.getLogger(Converter.class);

    /**
     * Deconstruct array list.
     *
     * @param g the g
     * @return the array list
     */
    public static ArrayList<Container> deconstruct(GameObject g) {
        HashMap<String, Container> containers = new HashMap<>();
        var array = new ArrayList<Container>();
        Container cont;
        if (g.componentExists(Texture.class)) {
            cont = new Container(g);
        } else {
            cont = new Container(g);
        }
        array.add(cont);
        //containers.put(g.getName(), cont);
        for (var c : g.getChildren()) {
            array.addAll(deconstruct(c));
        }
        return array;
    }

    /**
     * Construct game object.
     *
     * @param containerArrayList the container array list
     * @return the game object
     */
    public static GameObject construct(ArrayList<Container> containerArrayList) {
        try {
            var root = new GameObject("Root");
            for (var c : containerArrayList) {
                var obj = new GameObject("Obj", root);
                obj.setTransform(c.getX(), c.getY());
                if (!c.getTexture().equals("")) {
                    obj.addComponent(Texture.class).setTexture(c.getTexture());
                }
            }
            return root;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }
}
