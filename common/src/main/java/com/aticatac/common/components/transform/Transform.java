package com.aticatac.common.components.transform;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;

public class Transform extends Component {
    private Position position = new Position(0,0);

    private double rotation = 0;

    public Transform(GameObject gameObject) {
        super(gameObject);

//        if (gameObject.parent== null) return;
//
//        if(gameObject.parent instanceof GameObject){
//            Position p = ((GameObject)gameObject.parent).getComponent(TransformModel.class).position;
//            gameObject.getComponent(TransformModel.class).SetTransform(p.x,p.y);
//        }
    }

    public Position getPosition() {
        return position;
    }

    public double getX(){
        return getPosition().x;
    }

    public double getY(){
        return getPosition().y;
    }


    public void Transform(double x, double y) {
        this.SetTransform(position.x + x, position.y + y);
    }

    public void SetTransform(double x, double y) {
        double deltaX = position.x-x;
        double deltaY = position.y-y;

        for (var o:getGameObject().getChildren()) {
            o.getComponent(Transform.class).Transform(deltaX,deltaY);
        }

        SetTransformWithoutChild(x,y);
    }

    public void SetTransformWithoutChild(double x, double y) {
        position.x = x;
        position.y = y;
    }

    public void Forward(double v) {
        double xFactor = Math.sin(Math.toRadians(GetRotation()));
        double yFactor = -Math.cos(Math.toRadians(GetRotation()));
        Transform(v * xFactor, v * yFactor);
    }

    public void SetRotation(double r) {
        rotation = r;
        for (var o:getGameObject().getChildren()) {
            o.getComponent(Transform.class).SetRotation(r);
        }
    }

    public void Rotate(double r) {
        rotation = rotation + r;
        SetRotation(rotation);
    }

    public double GetRotation() {
        return rotation;
    }

    public void SetView(Position p){
        Position pRoot = getGameObject().getComponent(Transform.class).getPosition();
        Position pDelta = new Position(p.x-pRoot.x,p.y-pRoot.y);
        getGameObject().getComponent(Transform.class).SetTransform(pDelta.x,pDelta.y);
    }
}
