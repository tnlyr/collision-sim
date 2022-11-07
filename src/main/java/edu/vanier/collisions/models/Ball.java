package edu.vanier.collisions.models;

import javafx.scene.paint.Color;

public class Ball extends Entity {
    private Color color;
    private double radius;

    /**
     * Constructor
     * @param color
     * @param radius
     */
    public Ball(Color color, double radius, double posX, double speedX, double mass) {
        this.color = color;
        this.radius = radius;
        this.setPosX(posX);
        this.setSpeedX(speedX);
        this.setMass(mass);
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }


    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
