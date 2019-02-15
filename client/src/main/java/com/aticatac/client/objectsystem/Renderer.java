package com.aticatac.client.objectsystem;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.objectsystem.GameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Renderer extends Component {

    private Texture texture;

    public Texture getTexture() {return texture;}
    public void setTexture(Texture texture) {this.texture = texture;}
    public void setTexture(String texture) {this.texture = new Texture(Gdx.files.internal(texture));}

    /**
     * Instantiates a new Component.
     *
     * @param gameObject the component parent
     */
    public Renderer(GameObject gameObject) {
        super(gameObject);
    }

}
