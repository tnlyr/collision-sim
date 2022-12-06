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

    /**
     *
     * @return
     */
    public TranslateTransition getTranslateTransition() {
        return translateTransition;
    }

    /**
     *
     * @return
     */
    public double getInitialPosX() {
        return initialPosX;
    }

    /**
     *
     * @param initialPosX
     */
    public void setInitialPosX(double initialPosX) {
        this.initialPosX = initialPosX;
    }

    /**
     *
     * @return
     */
    public double getVelocityX() {
        return velocityX;
    }

    /**
     *
     * @param velocityX
     */
    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    /**
     *
     * @return
     */
    public double getCenterOffset() {
        return centerOffset;
    }

    /**
     *
     * @param centerOffset
     */
    public void setCenterOffset(double centerOffset) {
        this.centerOffset = centerOffset;
    }

    /**
     *
     * @return
     */
    public double getMass() {
        return mass;
    }

    /**
     *
     * @param mass
     */
    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * Allows to get the position in x
     * @return
     */
    public double getRelativePosition() {
        return getLayoutX() + getTranslateX() + centerOffset;
    }

    /**
     *
     * @param acceleration
     * @param time
     * @return The position of the car based on the time and acceleration taking into account your original position
     */
    public double getPositionAtTime(double acceleration, double time) {
        time = Math.min(time, getTimeAtStandstill(acceleration));
        return initialPosX + centerOffset + (velocityX * time) + (0.5 * acceleration * time * time);
    }

    /**
     *
     * @param acceleration
     * @param time
     * @return The velocity of the car based on the acceleration and time of the simulation
     */
    public double getVelocityAtTime(double acceleration, double time) {
        if (time > getTimeAtStandstill(acceleration)) {
            return 0;
        }
        return velocityX + 0.25 * acceleration * time;
    }

    /**
     *
     * @param acceleration
     * @return if they would not collide when would the car would stop based on the friction only.
     */
    public double getTimeAtStandstill(double acceleration) {
        double time = (0 - velocityX) / acceleration;
        return time < 0 ? Double.POSITIVE_INFINITY : time;
    }

    /**
     *
     * @return
     */
    public short getDirection() {
        if (velocityX == 0) {
            return 0;
        }
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
