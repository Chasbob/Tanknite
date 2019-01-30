package com.aticatac.client.component;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.objectsystem.GameObject;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Renderer extends Component {

    private Image baseImage;
    private Image image;

    private double rotation = 0;

    public Renderer(GameObject parent){
        super(parent);

        this.componentParent = parent;

        SetImage(image);
    }

    public void render(GraphicsContext gc)
    {
        //Position p = (componentParent.getComponent(Transform.class)).GetPosition();
        //gc.drawImage( image, p.x, p.y);
    }

    public void SetRotation(double r)
    {
        rotation = r;

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        ImageView iv = new ImageView(baseImage);
        iv.setRotate(rotation);

        image = iv.snapshot(params, null);
    }

    public void SetImage(Image i){
        baseImage = i;
        image = baseImage;
    }

    public Image getImage(){
        return image;
    }
}
