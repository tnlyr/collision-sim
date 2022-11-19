package edu.vanier.collisions.models;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.Rectangle;

public class PhysicsEntity extends Rectangle {
    private TranslateTransition translateTransition;
    private double initialPosX, speedX, mass;

    public PhysicsEntity() {
        super();
        translateTransition = new TranslateTransition();
        translateTransition.setNode(this);
        translateTransition.setInterpolator(Interpolator.LINEAR);
        setLayoutX(initialPosX);
    }

    public TranslateTransition getTranslateTransition() {
        return translateTransition;
    }

    public double getInitialPosX() {
        return initialPosX;
    }

    public void setInitialPosX(double initialPosX) {
        this.initialPosX = initialPosX;
    }

    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getRelativePosition() {
        int factor = (int) (speedX/Math.abs(speedX));
        return getLayoutX() + getTranslateX() + (factor*(getWidth() / 2));
    }

    public void play() {
        translateTransition.play();
    }

    public void pause() {
        translateTransition.pause();
    }

    public void reset() {
        translateTransition.stop();
        setTranslateX(0);
    }
}
