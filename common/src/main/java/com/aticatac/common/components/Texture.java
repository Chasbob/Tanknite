package com.aticatac.common.components;

import com.aticatac.common.objectsystem.GameObject;

public class Texture extends Component{
    private String Texture;
    /**
     * Instantiates a new Component.
     *
     * @param gameObject the component parent
     */
    public Texture(GameObject gameObject) {
        super(gameObject);
    }
    public void SetTexture(String texture){
        Texture = texture;
    }
    public String GetTexture(){
        return Texture;
    }
}
