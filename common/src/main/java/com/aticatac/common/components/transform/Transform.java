package com.aticatac.common.components.transform;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.Container;
import com.aticatac.common.objectsystem.GameObject;

/**
 * The type Transform.
 */
public class Transform extends Component {
    private Position position;
    private double rotation = 0;

    /**
     * Instantiates a new Component.
     *
     * @param gameObject the component parent
     */
    public Transform(GameObject gameObject) {
        super(gameObject);
        this.position = new Position(0, 0);
    }

    /**
     * Instantiates a new Transform.
     *
     * @param gameObject the game object
     * @param container  the container
     */
    public Transform(GameObject gameObject, Container container) {
        super(gameObject);
        this.position = new Position(container.getX(), container.getY());
        this.rotation = container.getR();
    }

    /**
     * Gets position.
     *
     * @return the position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets position.
     *
     * @param transform the transform
     */
    public void setPosition(Transform transform) {
        setPosition(transform.getX(), transform.getY());
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public double getX() {
        return this.position.getX();
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public double getY() {
        return this.position.getY();
    }

    /**
     * Apply transform.
     *
     * @param x the x
     * @param y the y
     */
    public void applyTransform(double x, double y) {
        this.setPosition(position.getX() + x, position.getY() + y);
    }

    /**
     * Sets position.
     *
     * @param x the x
     * @param y the y
     */
//  TODO REFACTOR
    public void setPosition(double x, double y) {
        double deltaX = position.getX() - x;
        double deltaY = position.getY() - y;
        for (var o : getGameObject().getChildren()) {
            o.getComponent(Transform.class).applyTransform(deltaX, deltaY);
        }
        setTransformWithoutChild(x, y);
    }

    private void setTransformWithoutChild(double x, double y) {
        position.setX(x);
        position.setY(y);
    }

    /**
     * Forward.
     *
     * @param v the v
     */
    public void forward(double v) {
        double xFactor = Math.sin(Math.toRadians(getRotation()));
        double yFactor = -Math.cos(Math.toRadians(getRotation()));
        applyTransform(v * xFactor, v * yFactor);
    }

    /**
     * Gets rotation.
     *
     * @return the rotation
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * Sets rotation.
     *
     * @param r the r
     */
    public void setRotation(double r) {
        rotation = r;
        //TODO wrap in public function as to allow different handling
        for (var o : getGameObject().getChildren()) {
            o.getComponent(Transform.class).setRotation(r);
        }
    }

    public void setPersonalRotation(double r) {
        this.rotation = r;
    }

    /**
     * Sets view.
     *
     * @param p the p
     */
    public void setView(Position p) {
        Position pRoot = getGameObject().getComponent(Transform.class).getPosition();
        Position pDelta = new Position(p.getX() - pRoot.getX(), p.getY() - pRoot.getY());
        getGameObject().getComponent(Transform.class).setPosition(pDelta.getX(), pDelta.getY());
    }

    @Override
    public String toString() {
        return "applyTransform{"
            +
            "position=" + position
            +
            ", rotation=" + rotation
            +
            '}'
            ;
    }
}
