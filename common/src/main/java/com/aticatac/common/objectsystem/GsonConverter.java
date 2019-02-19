package com.aticatac.common.objectsystem;

public class GsonConverter {
    public static RootObject Deconstructor(RootObject root){
        root.setChild(Deconstructor((GameObject) root.getChild()));
        return root;
    }

    private static GameObject Deconstructor(GameObject g){
        for (var c:g.fetchAllComponents()) {
            c.setGameObject(null);
        }

        for (var c:g.getChildren()){
            Deconstructor(c);
        }

        g.setChildren(null);
        return g;
    }
}
