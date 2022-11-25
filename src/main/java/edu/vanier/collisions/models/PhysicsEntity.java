package edu.vanier.collisions.models;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.Rectangle;
import java.io.Serializable;

public class PhysicsEntity extends Rectangle implements Serializable {
    private transient TranslateTransition translateTransition;
    private double initialPosX, velocityX, centerOffset, mass;

    public PhysicsEntity() {
        super();
        initializeTranslateTransition();
    }

    public void initializeTranslateTransition() {
        translateTransition = new TranslateTransition();
        translateTransition.setNode(this);
        translateTransition.setInterpolator(Interpolator.EASE_OUT);
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

    public double getCenterOffset() {
        return centerOffset;
    }

    public void setCenterOffset(double centerOffset) {
        this.centerOffset = centerOffset;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getRelativePosition() {
        return getLayoutX() + getTranslateX() + centerOffset;
    }

    public double getPositionAtTime(double acceleration, double time) {
        time = Math.min(time, getTimeAtStandstill(acceleration));
        return initialPosX + centerOffset + (velocityX * time) + (0.5 * acceleration * time * time);
    }

    public double getVelocityAtTime(double acceleration, double time) {
        if (time > getTimeAtStandstill(acceleration)) {
            return 0;
        }
        return velocityX + 0.25 * acceleration * time;
    }

    public double getTimeAtStandstill(double acceleration) {
        double time = (0 - velocityX) / acceleration;
        return time < 0 ? Double.POSITIVE_INFINITY : time;
    }

    public short getDirection() {
        return (short) (velocityX /Math.abs(velocityX));
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
        setLayoutX(initialPosX);
    }
}
