package edu.vanier.collisions.models;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.Rectangle;
import java.io.Serializable;

public class PhysicsEntity extends Rectangle implements Serializable {
    private transient TranslateTransition translateTransition;
    private double initialPosX, velocityX, mass;

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

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getRelativePosition() {
        int factor = (int) (velocityX /Math.abs(velocityX));
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
