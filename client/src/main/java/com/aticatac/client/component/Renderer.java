package com.aticatac.client.component;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.objectsystem.GameObject;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * The type Renderer.
 */
public class Renderer extends Component {
    private Image baseImage;
    private Image image;
    private double rotation = 0;

    /**
     * Instantiates a new Renderer.
     *
     * @param parent the parent
     */
    public Renderer(GameObject parent) {
        super(parent);
        setImage(image);
    }

    /**
     * Render.
     *
     * @param gc the gc
     */
    public void render(GraphicsContext gc) {
        Position p = (getGameObject().getComponent(Transform.class)).GetPosition();
        gc.drawImage(image, p.x, p.y);
    }

    /**
     * Sets rotation.
     *
     * @param r the r
     */
    public void setRotation(double r) {
        rotation = r;
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        ImageView iv = new ImageView(baseImage);
        iv.setRotate(rotation);
        image = iv.snapshot(params, null);
    }

    /**
     * Gets image.
     *
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Sets image.
     *
     * @param i the
     */
    public void setImage(Image i) {
        baseImage = i;
        image = baseImage;
    }
}
