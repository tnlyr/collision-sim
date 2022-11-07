package edu.vanier.collisions.models;

import javafx.scene.paint.Color;

public class Ball {
    private Color color;
    private double radius;

    private double posX;
    private double speedX;

    /**
     * Constructor
     * @param color
     * @param radius
     * @param posX
     * @param speedX
     */
    public Ball(Color color, double radius, double posX, double speedX) {
        this.color = color;
        this.radius = radius;
        this.posX = posX;
        this.speedX = speedX;
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

    /**
     * @return the posX
     */
    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    /**
     * @return the speedX
     */
    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }
}
